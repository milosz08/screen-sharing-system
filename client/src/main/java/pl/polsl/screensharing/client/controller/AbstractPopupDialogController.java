/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.client.model.ConnectionDetails;
import pl.polsl.screensharing.client.net.ClientDatagramSocket;
import pl.polsl.screensharing.client.net.ClientTcpSocket;
import pl.polsl.screensharing.client.net.ConnectionHandler;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.state.ConnectionState;
import pl.polsl.screensharing.client.state.VisibilityState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.fragment.VideoCanvas;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;

import javax.swing.*;

@Slf4j
@RequiredArgsConstructor
abstract class AbstractPopupDialogController implements ConnectionHandler {
    protected final ClientWindow clientWindow;
    protected final AbstractPopupDialog dialog;

    public void createConnection() {
        final ClientState state = clientWindow.getClientState();
        state.updateConnectionState(ConnectionState.CONNECTING);

        final ConnectionDetails connectionDetails = createConnectionParameters();
        if (connectionDetails != null) {
            final ClientTcpSocket clientTcpSocket = new ClientTcpSocket(clientWindow, this, connectionDetails);
            clientWindow.setClientTcpSocket(clientTcpSocket);
            clientTcpSocket.start();
        }
    }

    public void closeWindow() {
        dialog.closeWindow();
        dialog.dispose();
    }

    @Override
    public void onSuccess(ConnectionDetails connectionDetails) {
        final ClientState clientState = clientWindow.getClientState();
        final BottomInfobarController bottomInfobarController = clientWindow.getBottomInfobarController();
        final VideoCanvas videoCanvas = clientWindow.getVideoCanvas();

        clientState.updateConnectionState(ConnectionState.CONNECTED);
        bottomInfobarController.startConnectionTimer();

        onSuccessConnect(connectionDetails);
        closeWindow();

        final ClientDatagramSocket clientDatagramSocket = new ClientDatagramSocket(clientWindow, videoCanvas,
            videoCanvas.getController());
        clientWindow.setClientDatagramSocket(clientDatagramSocket);
        // uruchomienie wątku grabbera UDP transmisji danych z hosta
        clientDatagramSocket.start();
        clientState.updateVisibilityState(VisibilityState.VISIBLE);
    }

    @Override
    public void onFailure(ConnectionDetails connectionDetails, String message) {
        final ClientState clientState = clientWindow.getClientState();
        clientState.updateConnectionState(ConnectionState.DISCONNECTED);

        String errMessage = message;
        if (message == null) {
            errMessage = String.format("Cannot connect with %s:%s. Check connection parameters.",
                connectionDetails.getIpAddress(), connectionDetails.getPort());
        }
        log.info("Unable to connect with connection details: {}", connectionDetails);
        JOptionPane.showMessageDialog(dialog, errMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected abstract ConnectionDetails createConnectionParameters();
    protected abstract void onSuccessConnect(ConnectionDetails connectionDetails);
}
