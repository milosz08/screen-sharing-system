/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.gui;

import pl.polsl.screensharing.client.controller.TopMenuBarController;

import javax.swing.*;

public class TopMenuBar extends JMenuBar {
    private final TopMenuBarController controller;

    private final EstablishedConnectionWindow establishedConnectionWindow;
    private final LastConnectionsWindow lastConnectionsWindow;

    private final JMenu connectMenu = new JMenu("Connect");

    private final JMenuItem[] connectMenuItems = new JMenuItem[]{
        new JMenuItem("Established connection"),
        new JMenuItem("Last connections"),
        new JMenuItem("Disconnect"),
    };

    public TopMenuBar(ClientWindow clientWindow) {
        this.establishedConnectionWindow = new EstablishedConnectionWindow(clientWindow);
        this.lastConnectionsWindow = new LastConnectionsWindow(clientWindow);
        this.controller = new TopMenuBarController(clientWindow, this);

        connectMenuItems[0].addActionListener(e -> controller.openMakeConnectionWindow());
        connectMenuItems[1].addActionListener(e -> controller.openLastConnectionsWindow());
        connectMenuItems[2].addActionListener(e -> controller.disconnectFromSession());

        addMenuItems(connectMenu, connectMenuItems);
        add(connectMenu);
    }

    private void addMenuItems(JMenu menu, JMenuItem[] items) {
        for (final JMenuItem item : items) {
            menu.add(item);
        }
    }

    public EstablishedConnectionWindow getMakeConnectionWindow() {
        return establishedConnectionWindow;
    }

    public LastConnectionsWindow getLastConnectionsWindow() {
        return lastConnectionsWindow;
    }
}
