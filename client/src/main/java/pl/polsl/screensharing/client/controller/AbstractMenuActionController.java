/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.state.ConnectionState;
import pl.polsl.screensharing.client.state.RecordingState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.dialog.ConnectWindow;
import pl.polsl.screensharing.client.view.dialog.LastConnectionsWindow;

import javax.swing.*;

@Slf4j
@RequiredArgsConstructor
abstract class AbstractMenuActionController {
    protected final ClientWindow clientWindow;

    public void openMakeConnectionWindow() {
        final ConnectWindow window = clientWindow.getConnectWindow();
        window.setVisible(true);
    }

    public void openLastConnectionsWindow() {
        final LastConnectionsWindow window = clientWindow.getLastConnectionsWindow();
        window.setVisible(true);
    }

    public void disconnectFromSession() {
        final ClientState state = clientWindow.getClientState();
        final BottomInfobarController bottomInfobarController = clientWindow.getBottomInfobarController();

        final int result = JOptionPane.showConfirmDialog(clientWindow, "Are you sure to end up connection?",
            "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            state.updateConnectionState(ConnectionState.DISCONNECTED);
            bottomInfobarController.stopConnectionTimer();

            // TODO: disconnect from session

            log.info("Disconected from session.");
        }
    }

    public void takeScreenshot() {
        // TODO: take screenshot from canvas

        log.info("Taked screenshot");
    }

    public void startRecording() {
        final ClientState state = clientWindow.getClientState();
        final BottomInfobarController bottomInfobarController = clientWindow.getBottomInfobarController();

        state.updateRecordingState(RecordingState.RECORDING);
        bottomInfobarController.startRecordingTimer();

        // TODO: start recording session

        log.info("Started recording session");
    }

    public void stopRecording() {
        final ClientState state = clientWindow.getClientState();
        final BottomInfobarController bottomInfobarController = clientWindow.getBottomInfobarController();

        state.updateRecordingState(RecordingState.IDLE);
        bottomInfobarController.stopRecordingTimer();

        // TODO: stop recording session

        log.info("Stopped recording session");
    }
}
