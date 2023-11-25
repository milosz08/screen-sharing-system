/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.net.ServerDatagramSocket;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.StreamingState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.fragment.VideoCanvas;

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
            final ServerDatagramSocket serverDatagramSocket = new ServerDatagramSocket(hostWindow,
                videoCanvas.getController());
            hostWindow.setServerDatagramSocket(serverDatagramSocket);
            serverDatagramSocket.start();
            state.updateStreamingState(StreamingState.STREAMING);
            bottomInfoBarController.startStreamingTimer();
            log.info("Started screen streaming");
        }
    }

    public void stopVideoStreaming() {
        final HostState state = hostWindow.getHostState();
        final BottomInfobarController bottomInfoBarController = hostWindow.getBottomInfobarController();
        final ServerDatagramSocket serverDatagramSocket = hostWindow.getServerDatagramSocket();
        if (serverDatagramSocket != null) {
            serverDatagramSocket.stopAndClear();
            state.updateStreamingState(StreamingState.STOPPED);
        }
        bottomInfoBarController.stopStreamingTimer();
        log.info("Stopped screen streaming");
    }

    public void toggleScreenShowingForParticipants(boolean isShowing) {
        final HostState hostState = hostWindow.getHostState();
        hostState.updateShowingScreenForParticipants(isShowing);
        log.info("Turn {} showing screen for participants", isShowing ? "on" : "off");
    }
}
