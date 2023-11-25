/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.net;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.model.SessionDetails;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.StreamingState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.CryptoUtils;
import pl.polsl.screensharing.lib.Utils;
import pl.polsl.screensharing.lib.net.SocketState;
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
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class ClientThread extends Thread {
    private final Socket socket;
    private final HostWindow hostWindow;
    private final HostState hostState;
    private final SessionDetails sessionDetails;
    private final KeyPair serverKeyPair;
    private final ObjectMapper objectMapper;

    private PublicKey clientPublicKey;
    private SocketState socketState;
    private PrintWriter printWriter;

    public ClientThread(Socket socket, HostWindow hostWindow, SessionDetails sessionDetails, KeyPair serverKeyPair) {
        this.socket = socket;
        this.hostWindow = hostWindow;
        hostState = hostWindow.getHostState();
        this.sessionDetails = sessionDetails;
        this.serverKeyPair = serverKeyPair;
        socketState = SocketState.WAITING;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void run() {
        log.info("Started client thread with PID {}", getId());
        try (
            final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            printWriter = out;
            while (socket.isConnected()) {
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
        log.info("Stopping TCP client thread");
        log.debug("Collected detatched thread with TID {} by GC", getName());
        final ConcurrentMap<Long, ConnectedClientInfo> allClients = hostState.getLastEmittedConnectedClients();
        final ConnectedClientInfo removed = allClients.remove(getId());
        hostState.updateConnectedClients(allClients);
        log.info("Removed user with details {} from all connected users list", removed);
        try {
            socket.close();
        } catch (IOException ignore) {
        }
    }

    private void authenticationEventLoop(String data) throws Exception {
        switch (socketState) {
            // pobranie klucza publicznego od klienta i odesłanie do niego własnego klucza publicznego
            case EXHANGE_KEYS_REQ: {
                clientPublicKey = CryptoUtils.base64ToPublicKey(data);
                final String keyEnc = CryptoUtils.publicKeyToBase64(serverKeyPair.getPublic());
                printWriter.println(keyEnc);
                log.info("(To-way exchange) Save client public key and send server public key to the client");
                break;
            }
            // porównanie nadesłanego hasza hasła od klienta z hasłem sesji i wysłanie zestawu kluczy do szyfrowania
            // symetrycznego dla połączenia UDP
            case CHECK_PASSWORD_REQ: {
                performSSLExchange(data, decryptedObj -> {
                    boolean isValid = true;
                    if (sessionDetails.isHasPassword()) {
                        isValid = BCrypt.verifyer()
                            .verify(sessionDetails.getPassword().toCharArray(), decryptedObj.getPassword())
                            .verified;
                    }
                    final DatagramKeys datagramKeys = hostWindow.getDatagramKeys();
                    return AuthPasswordRes.builder()
                        .isValid(isValid)
                        .secretKeyUdp(datagramKeys.getSecretKey())
                        .secureRandomUdp(datagramKeys.getSecureRandom())
                        .build();
                }, rawResponse -> {
                    log.info("(To-way exchange) Checked client password with result: {}", rawResponse);
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

                    final ConcurrentMap<Long, ConnectedClientInfo> allClients = hostState
                        .getLastEmittedConnectedClients();
                    allClients.put(getId(), connectedClientInfo);
                    hostState.updateConnectedClients(allClients);

                    final StreamingState streamingState = hostState.getLastEmittedStreamingState();
                    return VideoFrameDetails.builder()
                        .aspectRatio(Utils.calcAspectRatio(hostWindow.getVideoCanvas()))
                        .isStreaming(streamingState.equals(StreamingState.STREAMING))
                        .build();
                }, rawResponse -> {
                    log.info("(To-way exchange) Persist client connection details {}", rawResponse);
                }, ConnectionData.class);
                break;
            }
        }
    }

    private <T> void performSSLExchange(
        String rawData, Function<T, Object> callback, Consumer<Object> onEnd, Class<T> parseClazz
    ) throws Exception {
        final String decrypted = CryptoUtils.rsaAsymDecrypt(rawData, serverKeyPair.getPrivate());
        final Object resData = callback.apply(objectMapper.readValue(decrypted, parseClazz));
        final String rawResponse = objectMapper.writeValueAsString(resData);
        final String encrypted = CryptoUtils.rsaAysmEncrypt(rawResponse, clientPublicKey);
        printWriter.println(encrypted);
        onEnd.accept(resData);
    }

    @Override
    public synchronized void start() {
        setName("Thread-TCP-Client-" + getId());
        if (!isAlive()) {
            super.start();
        }
    }
}
