/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.net.DatagramKeys;
import pl.polsl.screensharing.host.net.ServerDatagramSocket;
import pl.polsl.screensharing.host.net.ServerTcpSocket;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.StreamingState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.fragment.VideoCanvas;
import pl.polsl.screensharing.lib.net.SocketState;

import javax.swing.*;

@Slf4j
@RequiredArgsConstructor
abstract class AbstractStreamController {
    protected final HostWindow hostWindow;

    public void startVideoStreaming() {
        final HostState state = hostWindow.getHostState();
        final BottomInfobarController bottomInfoBarController = hostWindow.getBottomInfobarController();

        final int result = JOptionPane.showConfirmDialog(hostWindow, "Are you sure to start streaming your screen?",
            "Please confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            final VideoCanvas videoCanvas = hostWindow.getVideoCanvas();
            final DatagramKeys datagramKeys = hostWindow.getDatagramKeys();

            final ServerDatagramSocket serverDatagramSocket = new ServerDatagramSocket(hostWindow,
                videoCanvas.getController());
            serverDatagramSocket.createDatagramSocket(datagramKeys.getSecretKey(), datagramKeys.getSecureRandom(), 0);
            hostWindow.setServerDatagramSocket(serverDatagramSocket);
            serverDatagramSocket.start();

            final ServerTcpSocket serverTcpSocket = hostWindow.getServerTcpSocket();
            serverTcpSocket.sendSignalToAllClients(SocketState.EVENT_START_STREAMING);

            state.updateStreamingState(StreamingState.STREAMING);
            bottomInfoBarController.startStreamingTimer();
            log.info("Started screen streaming");
        }
    }

    public void stopVideoStreaming() {
        final HostState hostState = hostWindow.getHostState();
        final BottomInfobarController bottomInfoBarController = hostWindow.getBottomInfobarController();
        final ServerDatagramSocket serverDatagramSocket = hostWindow.getServerDatagramSocket();

        final ServerTcpSocket serverTcpSocket = hostWindow.getServerTcpSocket();
        serverTcpSocket.sendSignalToAllClients(SocketState.EVENT_STOP_STREAMING);

        if (serverDatagramSocket != null) {
            serverDatagramSocket.stopAndClear();
        }
        hostState.updateSendBytesPerSec(0L);
        bottomInfoBarController.stopStreamingTimer();
        log.info("Stopped screen streaming");
    }

    public void toggleScreenShowingForParticipants(boolean isShowing) {
        final HostState hostState = hostWindow.getHostState();
        final ServerTcpSocket serverTcpSocket = hostWindow.getServerTcpSocket();

        hostState.updateShowingScreenForParticipants(isShowing);
        if (hostState.getLastEmittedStreamingState().equals(StreamingState.STREAMING)) {
            serverTcpSocket.sendSignalToAllClients(SocketState.EVENT_TOGGLE_SCREEN_VISIBILITY);
        }
        if (!isShowing) {
            hostState.updateSendBytesPerSec(0L);
        }
        log.info("Turn {} showing screen for participants", isShowing ? "on" : "off");
    }
}
