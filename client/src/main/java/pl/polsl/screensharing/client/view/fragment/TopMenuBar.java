/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.fragment;

import pl.polsl.screensharing.client.controller.TopMenuBarController;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.AppIcon;
import pl.polsl.screensharing.lib.gui.component.JAppMenuIconItem;

import javax.swing.*;

public class TopMenuBar extends JMenuBar {
    private final TopMenuBarController controller;

    private final JMenu connectMenu = new JMenu("Connect");
    private final JMenu helpMenu = new JMenu("Help");

    private final JAppMenuIconItem[] connectMenuItems = {
        new JAppMenuIconItem("Established connection", AppIcon.ADD_CONNECTION),
        new JAppMenuIconItem("Disconnect", AppIcon.DISCONNECT),
        new JAppMenuIconItem("Last connections", AppIcon.CHECK_BOX_LIST),
    };

    private final JAppMenuIconItem[] helpMenuItems = {
        new JAppMenuIconItem("About", AppIcon.CODE_INFORMATION),
        new JAppMenuIconItem("License", AppIcon.CODE_INFORMATION_RULE),
    };

    public TopMenuBar(ClientWindow clientWindow) {
        this.controller = new TopMenuBarController(clientWindow);

        connectMenuItems[0].addActionListener(e -> controller.openMakeConnectionWindow());
        connectMenuItems[1].addActionListener(e -> controller.disconnectFromSession());
        connectMenuItems[2].addActionListener(e -> controller.openLastConnectionsWindow());

        helpMenuItems[0].addActionListener(e -> controller.openAboutProgramSection());
        helpMenuItems[1].addActionListener(e -> controller.openLicenseSection());

        addMenuItems(connectMenu, connectMenuItems);
        addMenuItems(helpMenu, helpMenuItems);

        add(connectMenu);
        add(helpMenu);
    }

    private void addMenuItems(JMenu menu, JMenuItem[] items) {
        for (final JMenuItem item : items) {
            menu.add(item);
        }
    }
}
