/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.fragment;

import lombok.Getter;
import pl.polsl.screensharing.client.controller.TopMenuBarController;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.state.ConnectionState;
import pl.polsl.screensharing.client.view.ClientIcon;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.gui.component.JAppMenuIconItem;
import pl.polsl.screensharing.lib.gui.icon.LibIcon;

import javax.swing.*;

@Getter
public class TopMenuBar extends JMenuBar {
    private final ClientState clientState;

    private final JMenu connectMenu;
    private final JMenu interactionMenu;
    private final JMenu helpMenu;

    private final JAppMenuIconItem createConnectionMenuItem;
    private final JAppMenuIconItem disconnectMenuItem;
    private final JAppMenuIconItem lastConnectionsMenuItem;

    private final JAppMenuIconItem takeScreenshotMenuItem;

    private final JAppMenuIconItem aboutMenuItem;
    private final JAppMenuIconItem licenseMenuItem;

    private final JAppMenuIconItem[] connectMenuItems;
    private final JAppMenuIconItem[] interactionMenuItems;
    private final JAppMenuIconItem[] helpMenuItems;

    private final TopMenuBarController controller;

    public TopMenuBar(ClientWindow clientWindow) {
        this.clientState = clientWindow.getClientState();
        this.controller = new TopMenuBarController(clientWindow);

        this.connectMenu = new JMenu("Connect");
        this.interactionMenu = new JMenu("Interaction");
        this.helpMenu = new JMenu("Help");

        this.createConnectionMenuItem = new JAppMenuIconItem("Connect", ClientIcon.ADD_CONNECTION);
        this.disconnectMenuItem = new JAppMenuIconItem("Disconnect", ClientIcon.DISCONNECT, false);
        this.lastConnectionsMenuItem = new JAppMenuIconItem("Last connections", LibIcon.CHECK_BOX_LIST);

        this.takeScreenshotMenuItem = new JAppMenuIconItem("Take screenshot", ClientIcon.TAKE_SNAPSHOT, false);

        this.aboutMenuItem = new JAppMenuIconItem("About", LibIcon.HELP_TABLE_OF_CONTENTS);
        this.licenseMenuItem = new JAppMenuIconItem("License", LibIcon.CODE_INFORMATION_RULE);

        initObservables();

        this.connectMenuItems = new JAppMenuIconItem[]{
            createConnectionMenuItem,
            disconnectMenuItem,
            lastConnectionsMenuItem
        };

        this.interactionMenuItems = new JAppMenuIconItem[]{
            takeScreenshotMenuItem,
        };

        this.helpMenuItems = new JAppMenuIconItem[]{
            aboutMenuItem,
            licenseMenuItem
        };

        this.createConnectionMenuItem.addActionListener(e -> controller.openMakeConnectionWindow());
        this.disconnectMenuItem.addActionListener(e -> controller.disconnectFromSession());
        this.lastConnectionsMenuItem.addActionListener(e -> controller.openLastConnectionsWindow());

        this.takeScreenshotMenuItem.addActionListener(e -> controller.takeScreenshot());

        this.aboutMenuItem.addActionListener(e -> controller.openAboutProgramSection());
        this.licenseMenuItem.addActionListener(e -> controller.openLicenseSection());

        addMenuItems(connectMenu, connectMenuItems);
        addMenuItems(interactionMenu, interactionMenuItems);
        addMenuItems(helpMenu, helpMenuItems);

        add(connectMenu);
        add(interactionMenu);
        add(helpMenu);
    }

    private void addMenuItems(JMenu menu, JMenuItem[] items) {
        for (final JMenuItem item : items) {
            menu.add(item);
        }
    }

    private void initObservables() {
        clientState.wrapAsDisposable(clientState.getConnectionState$(), state -> {
            final boolean isConnected = state.equals(ConnectionState.CONNECTED);
            createConnectionMenuItem.setEnabled(!isConnected);
            lastConnectionsMenuItem.setEnabled(!isConnected);
            disconnectMenuItem.setEnabled(isConnected);
            takeScreenshotMenuItem.setEnabled(isConnected);
        });
    }
}
