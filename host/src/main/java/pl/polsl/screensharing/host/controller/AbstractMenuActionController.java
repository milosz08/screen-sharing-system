/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.SessionState;
import pl.polsl.screensharing.host.state.StreamingState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.dialog.ConnectionSettingsWindow;
import pl.polsl.screensharing.host.view.fragment.TopMenuBar;
import pl.polsl.screensharing.host.view.fragment.TopToolbar;

import javax.swing.*;

@Slf4j
@RequiredArgsConstructor
abstract class AbstractMenuActionController {
    protected final HostWindow hostWindow;

    public void openSessionParamsWindow() {
        final ConnectionSettingsWindow window = hostWindow.getConnectionSettingsWindow();
        window.setVisible(true);
    }

    public void createSession() {
        final HostState state = hostWindow.getHostState();
        final BottomInfobarController bottomInfoBarController = hostWindow.getBottomInfobarController();

        state.setSessionState(SessionState.CREATED);
        updateSessionButtonsState(true);
        bottomInfoBarController.updateSessionStateUi();
        bottomInfoBarController.startSessionTimer();

        // TODO: creating session

        log.info("Created and started new session");

        JOptionPane.showMessageDialog(hostWindow,
            "Session was successfully created. Users can join with provided credentials.");
    }

    public void removeSession() {
        final HostState state = hostWindow.getHostState();
        final BottomInfobarController bottomInfoBarController = hostWindow.getBottomInfobarController();

        final int result = JOptionPane.showConfirmDialog(hostWindow, "Are you sure to remove current session?",
            "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            state.setSessionState(SessionState.INACTIVE);
            state.setStreamingState(StreamingState.STOPPED);

            updateSessionButtonsState(false);
            bottomInfoBarController.updateSessionStateUi();
            bottomInfoBarController.stopSessionTimer();

            // TODO: removing session

            log.info("Remove current session");
        }
    }

    public void startVideoStreaming() {
        final HostState state = hostWindow.getHostState();
        final BottomInfobarController bottomInfoBarController = hostWindow.getBottomInfobarController();

        final int result = JOptionPane.showConfirmDialog(hostWindow, "Are you sure to start streaming your screen?",
            "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            state.setStreamingState(StreamingState.STREAMING);

            updateVideoStreamButtonsState(true, state.isSessionCreated());
            bottomInfoBarController.startStreamingTimer();

            // TODO: starting screen sharing

            log.info("Started screen streaming");
        }
    }

    public void stopVideoStreaming() {
        final HostState state = hostWindow.getHostState();
        final BottomInfobarController bottomInfoBarController = hostWindow.getBottomInfobarController();

        state.setStreamingState(StreamingState.STOPPED);

        updateVideoStreamButtonsState(false, state.isSessionCreated());
        bottomInfoBarController.stopStreamingTimer();

        // TODO: stopping screen sharing

        log.info("Stopped screen streaming");
    }

    private void updateSessionButtonsState(boolean isSesionCreated) {
        final TopMenuBar topMenuBar = hostWindow.getTopMenuBar();
        final TopToolbar topToolbar = hostWindow.getTopToolbar();

        topMenuBar.getSessionParamsMenuItem().setEnabled(!isSesionCreated);
        topMenuBar.getCreateSessionMenuItem().setEnabled(!isSesionCreated);
        topMenuBar.getRemoveSessionMenuItem().setEnabled(isSesionCreated);

        topToolbar.getSessionParamsButton().setEnabled(!isSesionCreated);
        topToolbar.getCreateSessionButton().setEnabled(!isSesionCreated);
        topToolbar.getRemoveSessionButton().setEnabled(isSesionCreated);

        updateVideoStreamButtonsState(false, isSesionCreated);
    }

    private void updateVideoStreamButtonsState(boolean isVideoStreaming, boolean isSessionCreated) {
        final TopMenuBar topMenuBar = hostWindow.getTopMenuBar();
        final TopToolbar topToolbar = hostWindow.getTopToolbar();

        topMenuBar.getStartVideoStreamingMenuItem().setEnabled(!isVideoStreaming && isSessionCreated);
        topMenuBar.getStopVideoStreamingMenuItem().setEnabled(isVideoStreaming && isSessionCreated);

        topToolbar.getStartVideoStreamingButton().setEnabled(!isVideoStreaming && isSessionCreated);
        topToolbar.getStopVideoStreamingButton().setEnabled(isVideoStreaming && isSessionCreated);
    }
}
