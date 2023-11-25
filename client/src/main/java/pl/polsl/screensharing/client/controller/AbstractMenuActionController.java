/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.client.net.ClientDatagramSocket;
import pl.polsl.screensharing.client.net.ClientTcpSocket;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.state.ConnectionState;
import pl.polsl.screensharing.client.state.VisibilityState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.dialog.ConnectWindow;
import pl.polsl.screensharing.client.view.dialog.LastConnectionsWindow;
import pl.polsl.screensharing.lib.gui.file.FileUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

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
            "Please confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            final ClientDatagramSocket clientDatagramSocket = clientWindow.getClientDatagramSocket();
            final ClientTcpSocket clientTcpSocket = clientWindow.getClientTcpSocket();
            if (clientDatagramSocket != null) {
                clientDatagramSocket.stopAndClear();
                state.updateVisibilityState(VisibilityState.WAITING_FOR_CONNECTION);
            }
            if (clientTcpSocket != null) {
                clientTcpSocket.stopAndClear();
            }
            state.updateConnectionState(ConnectionState.DISCONNECTED);
            bottomInfobarController.stopConnectionTimer();
            log.info("Disconected from session.");
        }
    }

    public void takeScreenshot() {
        final VideoCanvasController videoCanvasController = clientWindow
            .getTabbedPaneWindow()
            .getTabbedVideoStreamPanel()
            .getVideoCanvas()
            .getController();
        if (videoCanvasController.getReceivedImage() == null) {
            return;
        }
        FileUtils.promptAndSaveFile("screenshot.jpg").ifPresent(file -> {
            try {
                ImageIO.write(videoCanvasController.getReceivedImage(), "jpg", file);
                JOptionPane.showMessageDialog(clientWindow, "Screeenshot saved to: " + file.getAbsolutePath());
                log.info("Taked screenshot and saved with name {}", file.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(clientWindow, "An error occurred while saving screenshot file!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
