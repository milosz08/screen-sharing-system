/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.state.HostState;
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
        state.setSessionCreated(true);
        updateSessionButtonsState(state.isSessionCreated());

        log.info("Created and started new session");

        // TODO: creating session

        JOptionPane.showMessageDialog(hostWindow,
            "Session was successfully created. Users can join with provided credentials.");
    }

    public void removeSession() {
        final HostState state = hostWindow.getHostState();

        final int result = JOptionPane.showConfirmDialog(hostWindow, "Are you sure to remove current session?",
            "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            log.info("Remove current session");

            state.setSessionCreated(false);
            state.setVideoStreaming(false);
            updateSessionButtonsState(state.isSessionCreated());

            // TODO: removing session
        }
    }

    public void startVideoStreaming() {
        final HostState state = hostWindow.getHostState();

        final int result = JOptionPane.showConfirmDialog(hostWindow, "Are you sure to start streaming your screen?",
            "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            log.info("Started screen streaming");

            state.setVideoStreaming(true);
            updateVideoStreamButtonsState(state.isSessionCreated());

            // TODO: starting screen sharing
        }
    }

    public void stopVideoStreaming() {
        final HostState state = hostWindow.getHostState();
        state.setVideoStreaming(false);
        updateVideoStreamButtonsState(state.isVideoStreaming());

        log.info("Stopped screen streaming");

        // TODO: stopping screen sharing
    }

    private void updateSessionButtonsState(boolean isSesionCreated) {
        final TopMenuBar topMenuBar = hostWindow.getTopMenuBar();
        final TopToolbar topToolbar = hostWindow.getTopToolbar();

        topMenuBar.getSessionParamsMenuItem().setEnabled(!isSesionCreated);
        topMenuBar.getCreateSessionMenuItem().setEnabled(!isSesionCreated);
        topMenuBar.getRemoveSessionMenuItem().setEnabled(isSesionCreated);
        topMenuBar.getStartVideoStreamingMenuItem().setEnabled(isSesionCreated);

        topToolbar.getSessionParamsButton().setEnabled(!isSesionCreated);
        topToolbar.getCreateSessionButton().setEnabled(!isSesionCreated);
        topToolbar.getRemoveSessionButton().setEnabled(isSesionCreated);
        topToolbar.getStartVideoStreamingButton().setEnabled(isSesionCreated);
    }

    private void updateVideoStreamButtonsState(boolean isVideoStreaming) {
        final TopMenuBar topMenuBar = hostWindow.getTopMenuBar();
        final TopToolbar topToolbar = hostWindow.getTopToolbar();

        topMenuBar.getStartVideoStreamingMenuItem().setEnabled(!isVideoStreaming);
        topMenuBar.getStopVideoStreamingMenuItem().setEnabled(isVideoStreaming);

        topToolbar.getStartVideoStreamingButton().setEnabled(!isVideoStreaming);
        topToolbar.getStopVideoStreamingButton().setEnabled(isVideoStreaming);
    }
}
