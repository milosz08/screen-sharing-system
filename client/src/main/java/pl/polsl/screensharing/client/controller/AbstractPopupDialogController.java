/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.client.dto.ConnDetailsDto;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.state.ConnectState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;

import javax.swing.*;

@Slf4j
abstract class AbstractPopupDialogController extends AbstractController {
    private final AbstractPopupDialog dialog;

    AbstractPopupDialogController(ClientWindow clientWindow, AbstractPopupDialog dialog) {
        super(clientWindow);
        this.dialog = dialog;
    }

    public void createConnection() {
        final ClientState state = clientWindow.getClientState();
        final BottomInfobarController bottomInfobarController = clientWindow.getBottomInfobarController();

        state.setConnectionState(ConnectState.CONNECTING);
        bottomInfobarController.updateConnectionStateUi();

        log.info("Started connecting...");

        final ConnDetailsDto connDetails = createConnectionParameters();
        if (connDetails == null) {
            return;
        }

        state.setConnectionState(ConnectState.CONNECTED);

        if (state.isConnected()) {
            bottomInfobarController.startConnectionTimer();
            bottomInfobarController.updateConnectionStateUi();
            updateConnectionButtonsState(true);
            onSuccessConnect(connDetails);
            closeWindow();
            log.info("Connection estabilished with connection details: {}", connDetails);
        } else {
            state.setConnectionState(ConnectState.DISCONNECTED);
            bottomInfobarController.stopConnectionTimer();
            bottomInfobarController.updateConnectionStateUi();
            
            log.info("Unable to connect with connection details: {}", connDetails);

            final String message = String.format("Cannot connect with %s:%s. Check connection parameters.",
                connDetails.getIpAddress(), connDetails.getPort());
            JOptionPane.showMessageDialog(dialog, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void closeWindow() {
        dialog.closeWindow();
        dialog.dispose();
    }

    protected abstract ConnDetailsDto createConnectionParameters();
    protected abstract void onSuccessConnect(ConnDetailsDto detailsDto);
}
