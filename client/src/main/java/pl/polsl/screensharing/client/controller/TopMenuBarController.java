/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.EstablishedConnectionWindow;
import pl.polsl.screensharing.client.view.LastConnectionsWindow;

import javax.swing.*;

public class TopMenuBarController {
    private final ClientWindow clientWindow;

    public TopMenuBarController(ClientWindow clientWindow) {
        this.clientWindow = clientWindow;
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
            // TODO: ending connection
            System.out.println("ending connection");
        }
    }
}
