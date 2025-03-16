package pl.polsl.screensharing.host.net;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.model.SessionDetails;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.StreamingState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.Utils;
import pl.polsl.screensharing.lib.net.CryptoAsymmetricHelper;
import pl.polsl.screensharing.lib.net.SocketState;
import pl.polsl.screensharing.lib.net.StreamingSignalState;
import pl.polsl.screensharing.lib.net.payload.AuthPasswordReq;
import pl.polsl.screensharing.lib.net.payload.AuthPasswordRes;
import pl.polsl.screensharing.lib.net.payload.ConnectionData;
import pl.polsl.screensharing.lib.net.payload.VideoFrameDetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.security.PublicKey;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class ClientThread extends Thread {
    @Getter
    private final Socket socket;
    private final HostWindow hostWindow;
    private final HostState hostState;
    private final SessionDetails sessionDetails;
    private final ObjectMapper objectMapper;
    private final SendSignalsThread sendSignalsThread;
    @Getter
    private final CryptoAsymmetricHelper cryptoAsymmetricHelper;
    private final Random random;

    @Getter
    private PublicKey clientPublicKey;
    private SocketState socketState;
    private PrintWriter printWriter;
    @Getter
    private long threadId;
    private ConcurrentMap<Long, ConnectedClientInfo> connectedClients;
    private boolean isThreadActive;

    public ClientThread(Socket socket, ServerTcpSocket serverTcpSocket) {
        this.socket = socket;
        this.hostWindow = serverTcpSocket.getHostWindow();
        hostState = hostWindow.getHostState();
        this.sessionDetails = serverTcpSocket.getSessionDetails();
        socketState = SocketState.WAITING;
        objectMapper = new ObjectMapper();
        cryptoAsymmetricHelper = serverTcpSocket.getCryptoAsymmetricHelper();
        random = new Random();
        sendSignalsThread = new SendSignalsThread(this, hostWindow);
        connectedClients = new ConcurrentHashMap<>();
        initObservables();
    }

    @Override
    public void run() {
        log.info("Started client thread with PID {}", getId());
        try (
            final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            printWriter = out;
            while (socket.isConnected() && isThreadActive) {
                final String line = in.readLine();
                if (line == null) {
                    break;
                }
                socketState = SocketState.extractHeader(line);
                authenticationEventLoop(SocketState.extractContent(line));
            }
        } catch (SocketException ignored) {
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        stopAndClose();
        System.gc();
    }

    public void sendSignalEvent(SocketState eventSignalState) {
        sendSignalsThread.setEventSignalState(eventSignalState);
    }

    public StreamingSignalState determinateStreamingState() {
        StreamingSignalState streamingSignalState;
        final StreamingState streamingState = hostState.getLastEmittedStreamingState();
        final Boolean isShowing = hostState.getLastEmittedIsScreenIsShowForParticipants();
        // jeśli streamowanie jest aktywne a ekran nie jest ukryty
        if (streamingState.equals(StreamingState.STREAMING)) {
            streamingSignalState = isShowing
                ? StreamingSignalState.STREAMING
                : StreamingSignalState.SCREEN_HIDDEN;
        } else {
            streamingSignalState = StreamingSignalState.STOPPED;
        }
        return streamingSignalState;
    }

    private void authenticationEventLoop(String data) throws Exception {
        switch (socketState) {
            // pobranie klucza publicznego od klienta i odesłanie do niego własnego klucza publicznego
            case EXHANGE_KEYS_REQ: {
                clientPublicKey = cryptoAsymmetricHelper.base64ToPublicKey(data);
                final String keyEnc = cryptoAsymmetricHelper.publicKeyToBase64();
                printWriter.println(keyEnc);
                log.info("(to-way exchange) Save client public key and send server public key to the client");
                break;
            }
            // porównanie nadesłanego hasza hasła od klienta z hasłem sesji i wysłanie zestawu kluczy do szyfrowania
            // symetrycznego dla połączenia UDP
            case CHECK_PASSWORD_REQ: {
                performSSLExchange(data, decryptedObj -> {
                    boolean isValid = true;
                    if (sessionDetails.getHasPassword()) {
                        isValid = BCrypt.verifyer()
                            .verify(sessionDetails.getPassword().toCharArray(), decryptedObj.getPassword())
                            .verified;
                    }
                    final DatagramKey datagramKey = hostWindow.getDatagramKey();
                    final AuthPasswordRes.AuthPasswordResBuilder builder = AuthPasswordRes.builder().validStatus(isValid);
                    if (!isValid) {
                        return builder.build();
                    }
                    return builder.secretKeyUdp(datagramKey.getSecretKey()).build();
                }, rawResponse -> {
                    log.info("(to-way exchange) Checked client password with result: {}", rawResponse);
                }, AuthPasswordReq.class);
                break;
            }
            // pobranie dodatkowych danych od klienta (ip, port, username) oraz odesłanie informacji o
            // strumieniu video
            case SEND_CLIENT_DATA_REQ: {
                performSSLExchange(data, decryptedObj -> {
                    final ConnectedClientInfo connectedClientInfo = ConnectedClientInfo.builder()
                        .clientThread(this)
                        .username(decryptedObj.getUsername())
                        .ipAddress(decryptedObj.getIpAddress())
                        .udpPort(decryptedObj.getUdpPort())
                        .build();

                    connectedClients.merge(threadId, connectedClientInfo, (existingList, newList) -> existingList);
                    hostState.updateConnectedClients(connectedClients);

                    // uruchomienie wątku wysyłającego zdarzenia do klientów poprzez event-loop
                    sendSignalsThread.start();

                    return VideoFrameDetails.builder()
                        .aspectRatio(Utils.calcAspectRatio(hostWindow.getVideoCanvas().getController().getRawImage()))
                        .streamingSignalState(determinateStreamingState())
                        .build();
                }, rawResponse -> {
                    log.info("(to-way exchange) Persist client connection details {}", rawResponse);
                }, ConnectionData.class);
                break;
            }
        }
    }

    private <T> void performSSLExchange(
        String rawData, Function<T, Object> callback, Consumer<Object> onEnd, Class<T> parseClazz
    ) throws Exception {
        final String decrypted = cryptoAsymmetricHelper.decrypt(rawData);
        final Object resData = callback.apply(objectMapper.readValue(decrypted, parseClazz));
        final String rawResponse = objectMapper.writeValueAsString(resData);
        final String encrypted = cryptoAsymmetricHelper.encrypt(rawResponse, clientPublicKey);
        printWriter.println(encrypted);
        onEnd.accept(resData);
    }

    @Override
    public synchronized void start() {
        if (!isAlive()) {
            threadId = generateRandomThreadNumber();
            setName("Thread-TCP-Client-" + threadId + "-" + getId());
            isThreadActive = true;
            super.start();
        }
    }

    private Long generateRandomThreadNumber() {
        final StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            stringBuffer.append(random.nextInt(9) + 1);
        }
        return Long.parseLong(stringBuffer.toString());
    }

    public void stopAndClose() {
        log.info("Stopping TCP client thread");
        log.debug("Collected detatched thread with TID {} by GC", getName());
        final ConnectedClientInfo removed = connectedClients.remove(threadId);
        hostState.updateConnectedClients(connectedClients);
        log.info("Removed user with details {} from all connected users list", removed);
        isThreadActive = false;
        try {
            socket.close();
        } catch (IOException ignore) {
        }
    }

    private void initObservables() {
        hostState.wrapAsDisposable(hostState.getConnectedClientsInfo$(), connectedClients -> {
            this.connectedClients = new ConcurrentHashMap<>(connectedClients);
        });
    }
}
