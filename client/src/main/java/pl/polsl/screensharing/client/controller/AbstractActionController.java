/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.dialog.EstablishedConnectionWindow;
import pl.polsl.screensharing.client.view.dialog.LastConnectionsWindow;

import javax.swing.*;

abstract class AbstractActionController {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractActionController.class);

    protected final ClientWindow clientWindow;
    private final ClientState state;

    AbstractActionController(ClientWindow clientWindow) {
        this.clientWindow = clientWindow;
        this.state = clientWindow.getCurrentState();
    }

    public void openMakeConnectionWindow() {
        final EstablishedConnectionWindow window = clientWindow.getEstablishedConnectionWindow();
        window.setVisible(true);
    }

    public void openLastConnectionsWindow() {
        final LastConnectionsWindow window = clientWindow.getLastConnectionsWindow();
        window.setVisible(true);
    }
    
    public void disconnectFromSession() {
        final int result = JOptionPane.showConfirmDialog(clientWindow, "Are you sure to end up connection?",
            "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            state.setConnectionEstabilished(false);
            clientWindow.getTopMenuBar().setConnectionButtonsState(false);
            clientWindow.getTopToolbar().setConnectionButtonsState(false);
            LOG.info("Disconected from session.");
        }
    }
}
