/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.client.model.ConnectionDetails;
import pl.polsl.screensharing.client.net.ClientDatagramSocket;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.state.ConnectionState;
import pl.polsl.screensharing.client.state.VisibilityState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.fragment.VideoCanvas;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;

import javax.swing.*;

@Slf4j
@RequiredArgsConstructor
abstract class AbstractPopupDialogController {
    protected final ClientWindow clientWindow;
    protected final AbstractPopupDialog dialog;

    public void createConnection() {
        final ClientState state = clientWindow.getClientState();
        final BottomInfobarController bottomInfobarController = clientWindow.getBottomInfobarController();

        state.updateConnectionState(ConnectionState.CONNECTING);

        log.info("Started connecting...");

        final ConnectionDetails connectionDetails = createConnectionParameters();
        if (connectionDetails == null) {
            return;
        }

        // TODO: connecting with host via TCP/IP Socket
        boolean isConnected = true;

        if (isConnected) {
            state.updateConnectionState(ConnectionState.CONNECTED);
            bottomInfobarController.startConnectionTimer();
            onSuccessConnect(connectionDetails);
            closeWindow();
            log.info("Connection estabilished with connection details: {}", connectionDetails);

            final VideoCanvas videoCanvas = clientWindow.getVideoCanvas();
            final ClientDatagramSocket clientDatagramSocket = new ClientDatagramSocket(clientWindow, videoCanvas,
                videoCanvas.getController());
            clientWindow.setClientDatagramSocket(clientDatagramSocket);
            // uruchomienie wątku grabbera UDP transmisji danych z hosta
            clientDatagramSocket.start();
            state.updateVisibilityState(VisibilityState.VISIBLE);
        } else {
            state.updateConnectionState(ConnectionState.DISCONNECTED);
            log.info("Unable to connect with connection details: {}", connectionDetails);

            final String message = String.format("Cannot connect with %s:%s. Check connection parameters.",
                connectionDetails.getIpAddress(), connectionDetails.getPort());
            JOptionPane.showMessageDialog(dialog, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void closeWindow() {
        dialog.closeWindow();
        dialog.dispose();
    }

    protected abstract ConnectionDetails createConnectionParameters();
    protected abstract void onSuccessConnect(ConnectionDetails connectionDetails);
}
