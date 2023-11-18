/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.StreamingState;
import pl.polsl.screensharing.host.view.HostWindow;

import javax.swing.*;

@Slf4j
@RequiredArgsConstructor
abstract class AbstractStreamController {
    protected final HostWindow hostWindow;

    public void startVideoStreaming() {
        final HostState state = hostWindow.getHostState();
        final BottomInfobarController bottomInfoBarController = hostWindow.getBottomInfobarController();

        final int result = JOptionPane.showConfirmDialog(hostWindow, "Are you sure to start streaming your screen?",
            "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            state.updateStreamingState(StreamingState.STREAMING);
            bottomInfoBarController.startStreamingTimer();

            // TODO: starting screen sharing

            log.info("Started screen streaming");
        }
    }

    public void stopVideoStreaming() {
        final HostState state = hostWindow.getHostState();
        final BottomInfobarController bottomInfoBarController = hostWindow.getBottomInfobarController();

        state.updateStreamingState(StreamingState.STOPPED);
        bottomInfoBarController.stopStreamingTimer();

        // TODO: stopping screen sharing

        log.info("Stopped screen streaming");
    }

    public void toggleScreenShowingForParticipants(boolean isShowing) {
        final HostState hostState = hostWindow.getHostState();
        hostState.updateShowingScreenForParticipants(isShowing);
        log.info("Turn {} showing screen for participants", isShowing ? "on" : "off");
    }
}
