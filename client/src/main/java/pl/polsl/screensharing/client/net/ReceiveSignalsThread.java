/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.net;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.client.model.ConnectionDetails;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.state.VisibilityState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.fragment.VideoCanvas;
import pl.polsl.screensharing.lib.CryptoUtils;
import pl.polsl.screensharing.lib.net.SocketState;
import pl.polsl.screensharing.lib.net.StreamingSignalState;
import pl.polsl.screensharing.lib.net.payload.AuthPasswordRes;
import pl.polsl.screensharing.lib.net.payload.VideoFrameDetails;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyPair;

@Slf4j
@RequiredArgsConstructor
public class ReceiveSignalsThread extends Thread {
    private final ClientTcpSocket clientTcpSocket;
    private final ClientWindow clientWindow;
    private final ClientState clientState;
    private final Socket socket;
    private final KeyPair keyPair;
    private final ObjectMapper objectMapper;

    private SocketState signalState;
    private String signalRawData;
    private boolean isKicked;

    public ReceiveSignalsThread(ClientTcpSocket clientTcpSocket) {
        this.clientTcpSocket = clientTcpSocket;
        clientWindow = clientTcpSocket.getClientWindow();
        clientState = clientTcpSocket.getClientState();
        socket = clientTcpSocket.getClientSocket();
        keyPair = clientTcpSocket.getClientKeypair();
        objectMapper = new ObjectMapper();
        signalState = SocketState.WAITING;
    }

    @Override
    public void run() {
        log.info("Starting TCP client receive events thread...");
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (socket.isConnected() && !isKicked) {
                final String line = in.readLine();
                if (line == null) {
                    break;
                }
                signalState = SocketState.extractHeader(line);
                signalRawData = SocketState.extractContent(line);
                switch (signalState) {
                    // sygnał w przypadku uruchomienia streamowania ekranu przez hosta
                    case EVENT_START_STREAMING: {
                        final VideoFrameDetails videoFrameDetails = exchangeSSLRequest(VideoFrameDetails.class);
                        final StreamingSignalState state = videoFrameDetails.getStreamingSignalState();

                        clientState.updateVisibilityState(VisibilityState.getBasedSignalState(state));
                        clientState.updateFrameAspectRation(videoFrameDetails.getAspectRatio());

                        startDatagramThread();

                        signalState = SocketState.WAITING;
                        log.info("(signal event) Receive start sharing screen event with data {}", videoFrameDetails);
                        break;
                    }
                    // sygnał w przypadku zatrzymania streamowania ekranu przez hosta
                    case EVENT_STOP_STREAMING: {
                        clientState.updateVisibilityState(VisibilityState.WAITING_FOR_CONNECTION);

                        signalState = SocketState.WAITING;
                        log.info("(signal event) Receive stop sharing screen event");
                        break;
                    }
                    // sygnał w przypadku pokazania/showania ekranu przez hosta
                    case EVENT_TOGGLE_SCREEN_VISIBILITY: {
                        final VideoFrameDetails videoFrameDetails = exchangeSSLRequest(VideoFrameDetails.class);
                        final StreamingSignalState state = videoFrameDetails.getStreamingSignalState();

                        clientState.updateVisibilityState(VisibilityState.getBasedSignalState(state));
                        if (state.equals(StreamingSignalState.STREAMING)) {
                            clientState.updateFrameAspectRation(videoFrameDetails.getAspectRatio());
                            startDatagramThread();
                        }
                        signalState = SocketState.WAITING;
                        log.info("(signal event) Receive show/hide screen event with data {}", videoFrameDetails);
                        break;
                    }
                    // sygnał w przypadku wyrzucenia użytkownika z sesji
                    case KICK_FROM_SESSION: {
                        isKicked = true;
                        log.info("(signal event) Receive kick from session event");
                        break;
                    }
                }
            }
        } catch (SocketException ex) {
            clientTcpSocket.stopAndClear();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (isKicked) {
            clientTcpSocket.stopAndClear();
            JOptionPane.showMessageDialog(null, "You has been kicked from session", "Info",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void startDatagramThread() {
        final VideoCanvas videoCanvas = clientWindow.getVideoCanvas();
        final AuthPasswordRes res = clientTcpSocket.getAuthPasswordRes();
        final ConnectionDetails details = clientTcpSocket.getConnectionDetails();

        // uruchomienie grabbera do odbierania strumienia obrazu tylko w przypadku gdy nie jest jeszcze
        // uruchomiony, bądź przy ponownym uruchomieniu (wątek w przypadku braku obrazu jest joinowany do głównego
        // wątku aby zaoszczędzić zasoby)
        if (!clientWindow.getClientDatagramSocket().isAlive()) {
            final ClientDatagramSocket clientDatagramSocket = new ClientDatagramSocket(clientWindow,
                videoCanvas, videoCanvas.getController());

            clientWindow.setClientDatagramSocket(clientDatagramSocket);
            clientDatagramSocket.createDatagramSocket(res.getSecretKeyUdp(), res.getSecureRandomUdp(),
                details.getClientPort());

            clientDatagramSocket.start();
        }
    }

    private <T> T exchangeSSLRequest(Class<T> parseClazz) throws Exception {
        final String decrypted = CryptoUtils.rsaAsymDecrypt(signalRawData, keyPair.getPrivate());
        return objectMapper.readValue(decrypted, parseClazz);
    }

    @Override
    public synchronized void start() {
        if (!isAlive()) {
            setName("Thread-TCP-Event-Recv-" + getId());
            super.start();
        }
    }
}
