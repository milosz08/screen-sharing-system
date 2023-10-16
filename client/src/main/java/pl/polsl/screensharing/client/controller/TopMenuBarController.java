/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import pl.polsl.screensharing.client.gui.ClientWindow;
import pl.polsl.screensharing.client.gui.EstablishedConnectionWindow;
import pl.polsl.screensharing.client.gui.LastConnectionsWindow;
import pl.polsl.screensharing.client.gui.TopMenuBar;

import javax.swing.*;

public class TopMenuBarController {
    private final ClientWindow clientWindow;
    private final TopMenuBar topMenuBar;

    public TopMenuBarController(ClientWindow clientWindow, TopMenuBar topMenuBar) {
        this.clientWindow = clientWindow;
        this.topMenuBar = topMenuBar;
    }

    public void openMakeConnectionWindow() {
        final EstablishedConnectionWindow window = topMenuBar.getMakeConnectionWindow();
        window.setVisible(true);
    }

    public void openLastConnectionsWindow() {
        final LastConnectionsWindow window = topMenuBar.getLastConnectionsWindow();
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
