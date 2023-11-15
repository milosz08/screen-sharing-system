/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.CaptureFramelessWindow;
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
        updateSessionButtonsState(true);

        // TODO: creating session

        log.info("Created and started new session");

        final CaptureFramelessWindow captureFramelessWindow = hostWindow.getCaptureFramelessWindow();
        captureFramelessWindow.setVisible(true);

        JOptionPane.showMessageDialog(hostWindow,
            "Session was successfully created. Users can join with provided credentials.");
    }

    public void removeSession() {
        final HostState state = hostWindow.getHostState();
        final CaptureController captureController = hostWindow.getCaptureFramelessWindow().getCaptureController();

        final int result = JOptionPane.showConfirmDialog(hostWindow, "Are you sure to remove current session?",
            "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            state.setSessionCreated(false);
            state.setVideoStreaming(false);
            updateSessionButtonsState(false);
            captureController.toggleFrameBorder(false);

            // TODO: removing session

            final CaptureFramelessWindow captureFramelessWindow = hostWindow.getCaptureFramelessWindow();
            captureFramelessWindow.setVisible(false);

            log.info("Remove current session");
        }
    }

    public void startVideoStreaming() {
        final HostState state = hostWindow.getHostState();
        final CaptureController captureController = hostWindow.getCaptureFramelessWindow().getCaptureController();

        toggleFramelessCaptureFrame(true);

        final int result = JOptionPane.showConfirmDialog(hostWindow, "Are you sure to start streaming your screen?",
            "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            captureController.toggleFrameBorder(true);
            state.setVideoStreaming(true);
            updateVideoStreamButtonsState(true, state.isSessionCreated());

            // TODO: starting screen sharing

            log.info("Started screen streaming");
        }
    }

    public void stopVideoStreaming() {
        final HostState state = hostWindow.getHostState();
        final CaptureController captureController = hostWindow.getCaptureFramelessWindow().getCaptureController();

        state.setVideoStreaming(false);
        captureController.toggleFrameBorder(false);

        updateVideoStreamButtonsState(false, state.isSessionCreated());
        toggleFramelessCaptureFrame(true);

        // TODO: stopping screen sharing

        log.info("Stopped screen streaming");
    }

    public void toggleFramelessCaptureFrame(boolean isActive) {
        final HostState state = hostWindow.getHostState();
        final CaptureFramelessWindow captureFramelessWindow = hostWindow.getCaptureFramelessWindow();
        captureFramelessWindow.setVisible(isActive);
        updateFramelessVisibilityButtonsState(!isActive, state.isSessionCreated());
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
        updateFramelessVisibilityButtonsState(false, isSesionCreated);
    }

    private void updateVideoStreamButtonsState(boolean isVideoStreaming, boolean isSessionCreated) {
        final TopMenuBar topMenuBar = hostWindow.getTopMenuBar();
        final TopToolbar topToolbar = hostWindow.getTopToolbar();

        topMenuBar.getStartVideoStreamingMenuItem().setEnabled(!isVideoStreaming && isSessionCreated);
        topMenuBar.getStopVideoStreamingMenuItem().setEnabled(isVideoStreaming && isSessionCreated);

        topToolbar.getStartVideoStreamingButton().setEnabled(!isVideoStreaming && isSessionCreated);
        topToolbar.getStopVideoStreamingButton().setEnabled(isVideoStreaming && isSessionCreated);
    }

    private void updateFramelessVisibilityButtonsState(boolean isFrameVisible, boolean isSessionCreated) {
        final TopMenuBar topMenuBar = hostWindow.getTopMenuBar();
        final TopToolbar topToolbar = hostWindow.getTopToolbar();

        topMenuBar.getShowFramelessCaptureMenuItem().setEnabled(isFrameVisible && isSessionCreated);
        topMenuBar.getHideFramelessCaptureMenuItem().setEnabled(!isFrameVisible && isSessionCreated);

        topToolbar.getShowFramelessCaptureButton().setEnabled(isFrameVisible && isSessionCreated);
        topToolbar.getHideFramelessCaptureButton().setEnabled(!isFrameVisible && isSessionCreated);
    }
}
