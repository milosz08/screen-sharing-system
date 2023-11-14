/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.fragment;

import pl.polsl.screensharing.client.controller.TopMenuBarController;
import pl.polsl.screensharing.client.view.ClientIcon;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.gui.component.JAppMenuIconItem;
import pl.polsl.screensharing.lib.gui.icon.LibIcon;

import javax.swing.*;

public class TopMenuBar extends JMenuBar {
    private final TopMenuBarController controller;

    private final JMenu connectMenu;
    private final JMenu helpMenu;

    private final JAppMenuIconItem estabilishedConnectionMenuItem;
    private final JAppMenuIconItem disconnectMenuItem;
    private final JAppMenuIconItem lastConnectionsMenuItem;

    private final JAppMenuIconItem aboutMenuItem;
    private final JAppMenuIconItem licenseMenuItem;

    private final JAppMenuIconItem[] connectMenuItems;
    private final JAppMenuIconItem[] helpMenuItems;

    public TopMenuBar(ClientWindow clientWindow) {
        this.controller = new TopMenuBarController(clientWindow);

        this.connectMenu = new JMenu("Connect");
        this.helpMenu = new JMenu("Help");

        this.estabilishedConnectionMenuItem = new JAppMenuIconItem("Established connection", AppIcon.ADD_CONNECTION);
        this.disconnectMenuItem = new JAppMenuIconItem("Disconnect", AppIcon.DISCONNECT, false);
        this.lastConnectionsMenuItem = new JAppMenuIconItem("Last connections", AppIcon.CHECK_BOX_LIST);

        this.aboutMenuItem = new JAppMenuIconItem("About", AppIcon.CODE_INFORMATION);
        this.licenseMenuItem = new JAppMenuIconItem("License", AppIcon.CODE_INFORMATION_RULE);

        this.connectMenuItems = new JAppMenuIconItem[]{
            estabilishedConnectionMenuItem,
            disconnectMenuItem,
            lastConnectionsMenuItem
        };

        this.helpMenuItems = new JAppMenuIconItem[]{
            aboutMenuItem,
            licenseMenuItem
        };

        this.estabilishedConnectionMenuItem.addActionListener(e -> controller.openMakeConnectionWindow());
        this.disconnectMenuItem.addActionListener(e -> controller.disconnectFromSession());
        this.lastConnectionsMenuItem.addActionListener(e -> controller.openLastConnectionsWindow());

        this.aboutMenuItem.addActionListener(e -> controller.openAboutProgramSection());
        this.licenseMenuItem.addActionListener(e -> controller.openLicenseSection());

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

    public void setConnectionButtonsState(boolean onIsConnect) {
        this.estabilishedConnectionMenuItem.setEnabled(!onIsConnect);
        this.disconnectMenuItem.setEnabled(onIsConnect);
        this.lastConnectionsMenuItem.setEnabled(!onIsConnect);
    }
}
