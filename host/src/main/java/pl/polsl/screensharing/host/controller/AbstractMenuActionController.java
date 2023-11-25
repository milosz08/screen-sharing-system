/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.net.ServerDatagramSocket;
import pl.polsl.screensharing.host.net.ServerTcpSocker;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.SessionState;
import pl.polsl.screensharing.host.state.StreamingState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.dialog.SessionDetailsDialogWindow;

import javax.swing.*;

@Slf4j
abstract class AbstractMenuActionController extends AbstractStreamController {
    public AbstractMenuActionController(HostWindow hostWindow) {
        super(hostWindow);
    }

    public void openSessionDetailsWindow() {
        final SessionDetailsDialogWindow window = hostWindow.getSessionDetailsDialogWindow();
        window.setVisible(true);
    }

    public void removeSession() {
        final HostState hostState = hostWindow.getHostState();
        final BottomInfobarController bottomInfoBarController = hostWindow.getBottomInfobarController();

        final int result = JOptionPane.showConfirmDialog(hostWindow, "Are you sure to remove current session?",
            "Please confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            final ServerDatagramSocket serverDatagramSocket = hostWindow.getServerDatagramSocket();
            final ServerTcpSocker serverTcpSocker = hostWindow.getServerTcpSocker();

            if (serverDatagramSocket != null) {
                serverDatagramSocket.stopAndClear();
                bottomInfoBarController.stopSessionTimer();
                hostState.updateStreamingState(StreamingState.STOPPED);
            }
            if (serverTcpSocker != null) {
                serverTcpSocker.stopAndClear();
                hostState.updateSessionState(SessionState.INACTIVE);
            }
        }
    }
}
