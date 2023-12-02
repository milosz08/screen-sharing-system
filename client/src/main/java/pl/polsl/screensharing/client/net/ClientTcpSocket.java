/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.net;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.client.controller.BottomInfobarController;
import pl.polsl.screensharing.client.model.ConnectionDetails;
import pl.polsl.screensharing.client.model.FastConnectionDetails;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.state.ConnectionState;
import pl.polsl.screensharing.client.state.VisibilityState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.fragment.VideoCanvas;
import pl.polsl.screensharing.lib.net.AbstractTcpSocketThread;
import pl.polsl.screensharing.lib.net.SocketState;
import pl.polsl.screensharing.lib.net.StreamingSignalState;
import pl.polsl.screensharing.lib.net.payload.AuthPasswordReq;
import pl.polsl.screensharing.lib.net.payload.AuthPasswordRes;
import pl.polsl.screensharing.lib.net.payload.ConnectionData;
import pl.polsl.screensharing.lib.net.payload.VideoFrameDetails;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.security.GeneralSecurityException;

@Slf4j
public class ClientTcpSocket extends AbstractTcpSocketThread<Socket> {
    private SocketState socketState;
    @Getter
    private AuthPasswordRes authPasswordRes;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private boolean isThreadActive;

    @Getter
    private final ClientWindow clientWindow;
    @Getter
    private final ClientState clientState;
    private final FastConnectionDetails fastConnectionDetails;
    private final ClientDatagramSocket clientDatagramSocket;
    private final ConnectionHandler connectionHandler;
    @Getter
    private final ConnectionDetails connectionDetails;
    private final ObjectMapper objectMapper;

    public ClientTcpSocket(
        ClientWindow clientWindow, ConnectionHandler connectionHandler, ConnectionDetails connectionDetails
    ) {
        super();
        this.clientWindow = clientWindow;
        clientState = clientWindow.getClientState();
        clientDatagramSocket = clientWindow.getClientDatagramSocket();
        fastConnectionDetails = clientWindow.getClientState().getLastEmittedFastConnectionDetails();
        this.connectionHandler = connectionHandler;
        this.connectionDetails = connectionDetails;
        socketState = SocketState.EXHANGE_KEYS_REQ;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void run() {
        log.info("Starting TCP client thread...");
        try (
            final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            bufferedReader = in;
            printWriter = out;
            while (isThreadActive) {
                switch (socketState) {
                    // wysłanie klucza publicznego klienta do hosta (serwera)
                    case EXHANGE_KEYS_REQ: {
                        final String keyEnc = cryptoAsymmetricHelper.publicKeyToBase64();
                        out.println(SocketState.EXHANGE_KEYS_REQ.generateBody(keyEnc));
                        socketState = SocketState.EXHANGE_KEYS_RES;
                        log.info("Send public RSA key to server");
                        break;
                    }
                    // otrzymanie klucza publicznego od hosta i zapisanie go w pamięci
                    case EXHANGE_KEYS_RES: {
                        exchangePublicKey = cryptoAsymmetricHelper.base64ToPublicKey(readData());
                        socketState = SocketState.CHECK_PASSWORD_REQ;
                        log.info("Persist public RSA key from server and save in-memory storage");
                        break;
                    }
                    // wysłanie haszu hasła do sprawdzenia do hosta (serwera)
                    case CHECK_PASSWORD_REQ: {
                        final String passwordHash = BCrypt.withDefaults()
                            .hashToString(10, connectionDetails.getPassword().toCharArray());
                        final AuthPasswordReq req = new AuthPasswordReq(passwordHash);
                        exchangeSSLRequest(req, SocketState.CHECK_PASSWORD_REQ, SocketState.CHECK_PASSWORD_RES);
                        log.info("Send password for check authority by server");
                        break;
                    }
                    // odebranie wiadomości o poprawności hasła oraz klucz do szyfrowanie symetrycznego UDP
                    case CHECK_PASSWORD_RES: {
                        final AuthPasswordRes res = exchangeSSLResponse(AuthPasswordRes.class);
                        if (!res.isValidStatus()) {
                            connectionHandler.onFailure(connectionDetails, "Invalid password");
                            log.warn("Invalid password. Disconnect from session");
                            break;
                        }
                        authPasswordRes = res;
                        socketState = SocketState.SEND_CLIENT_DATA_REQ;
                        log.info("Successfully validated password.");
                        break;
                    }
                    // wysłanie dodatkowych danych (adres ip, port UDP, nazwa użytkownika)
                    case SEND_CLIENT_DATA_REQ: {
                        final VideoCanvas videoCanvas = clientWindow.getVideoCanvas();
                        final ClientDatagramSocket clientDatagramSocket = new ClientDatagramSocket(clientWindow,
                            videoCanvas, videoCanvas.getController());

                        clientDatagramSocket.createDatagramSocket(authPasswordRes.getSecretKeyUdp(),
                            connectionDetails.getClientPort());
                        clientWindow.setClientDatagramSocket(clientDatagramSocket);

                        final ConnectionData connectionData = ConnectionData.builder()
                            .username(connectionDetails.getUsername())
                            .ipAddress(connectionDetails.getClientIpAddress())
                            .udpPort(connectionDetails.getClientPort())
                            .build();

                        exchangeSSLRequest(connectionData, SocketState.SEND_CLIENT_DATA_REQ,
                            SocketState.SEND_CLIENT_DATA_RES);
                        log.info("Successfully sent client connection details");
                        break;
                    }
                    // odebranie od hosta (serwera) dodatkowych informacji, czy stream jest aktywny oraz
                    // aspect ratio obrazu
                    case SEND_CLIENT_DATA_RES: {
                        final VideoFrameDetails videoFrameDetails = exchangeSSLResponse(VideoFrameDetails.class);
                        final StreamingSignalState state = videoFrameDetails.getStreamingSignalState();

                        clientState.updateVisibilityState(VisibilityState.getBasedSignalState(state));
                        clientState.updateFrameAspectRation(videoFrameDetails.getAspectRatio());

                        // stworzenie i uruchomienie wątku pętli zdarzeń z hosta
                        final ReceiveSignalsThread receiveSignalsThread = new ReceiveSignalsThread(this);
                        receiveSignalsThread.start();

                        // uruchomienie wątku grabbera UDP
                        clientWindow.getClientDatagramSocket().start();
                        connectionHandler.onSuccess(connectionDetails);

                        socketState = SocketState.WAITING;
                        log.info("Successfully got video frame details {}", videoFrameDetails);
                        break;
                    }
                }
            }
        } catch (SocketException ignored) {
            stopAndClear();
        } catch (Exception ex) {
            stopAndClear();
            log.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exchangeSSLRequest(Object req, SocketState sendState, SocketState nextState) throws Exception {
        final String parsedJson = objectMapper.writeValueAsString(req);
        final String encrypted = cryptoAsymmetricHelper.encrypt(parsedJson, exchangePublicKey);
        printWriter.println(sendState.generateBody(encrypted));
        socketState = nextState;
    }

    private <T> T exchangeSSLResponse(Class<T> parseClass) throws Exception {
        final String decrypted = cryptoAsymmetricHelper.decrypt(readData());
        return objectMapper.readValue(decrypted, parseClass);
    }

    private String readData() throws Exception {
        final String data = bufferedReader.readLine();
        if (data == null) {
            throw new SocketException();
        }
        return data;
    }

    @Override
    public synchronized void startExecutor() {
        try {
            initSocketAndKeys();
            startThread();
            isThreadActive = true;
        } catch (IOException | GeneralSecurityException ex) {
            log.error(ex.getMessage());
            connectionHandler.onFailure(connectionDetails, null);
        }
    }

    @Override
    public void abstractStopAndClear() {
        final BottomInfobarController bottomInfobarController = clientWindow.getBottomInfobarController();
        log.info("Disconnected with host: {}:{}", fastConnectionDetails.getHostIpAddress(),
            fastConnectionDetails.getHostPort());

        clientState.updateRecvBytesPerSec(0L);
        clientState.updateVisibilityState(VisibilityState.WAITING_FOR_CONNECTION);
        clientState.updateConnectionState(ConnectionState.DISCONNECTED);
        bottomInfobarController.stopConnectionTimer();

        if (clientDatagramSocket != null) {
            isThreadActive = false;
            clientDatagramSocket.stopAndClear();
        }
    }

    @Override
    protected void createTcpSocket() throws IOException {
        final String ipAddress = fastConnectionDetails.getHostIpAddress();
        final int port = fastConnectionDetails.getHostPort();
        socket = new Socket();
        socket.connect(new InetSocketAddress(ipAddress, port));
        log.info("Successfully created connection with {}:{} server", ipAddress, port);
    }
}
