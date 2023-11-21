/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.SessionState;
import pl.polsl.screensharing.host.state.StreamingState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.dialog.ConnectionSettingsDialogWindow;

import javax.swing.*;

@Slf4j
abstract class AbstractMenuActionController extends AbstractStreamController {
    public AbstractMenuActionController(HostWindow hostWindow) {
        super(hostWindow);
    }

    public void openSessionParamsWindow() {
        final ConnectionSettingsDialogWindow window = hostWindow.getConnectionSettingsDialogWindow();
        window.setVisible(true);
    }

    public void createSession() {
        final HostState state = hostWindow.getHostState();
        final BottomInfobarController bottomInfoBarController = hostWindow.getBottomInfobarController();

        state.updateSessionState(SessionState.CREATED);
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
            state.updateSessionState(SessionState.INACTIVE);
            state.updateStreamingState(StreamingState.STOPPED);
            bottomInfoBarController.stopSessionTimer();

            // TODO: removing session

            log.info("Remove current session");
        }
    }
}
