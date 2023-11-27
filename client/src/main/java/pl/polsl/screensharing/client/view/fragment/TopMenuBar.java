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
    private final JMenu sessionInfoMenu;
    private final JMenu interactionMenu;
    private final JMenu helpMenu;

    private final JAppMenuIconItem createConnectionMenuItem;
    private final JAppMenuIconItem disconnectMenuItem;
    private final JAppMenuIconItem lastConnectionsMenuItem;
    private final JAppMenuIconItem sessionInfoMenuItem;

    private final JAppMenuIconItem takeScreenshotMenuItem;

    private final JAppMenuIconItem aboutMenuItem;
    private final JAppMenuIconItem licenseMenuItem;

    private final JAppMenuIconItem[] connectMenuItems;
    private final JAppMenuIconItem[] sessionInfoMenuItems;
    private final JAppMenuIconItem[] interactionMenuItems;
    private final JAppMenuIconItem[] helpMenuItems;

    private final TopMenuBarController controller;

    public TopMenuBar(ClientWindow clientWindow) {
        clientState = clientWindow.getClientState();
        controller = new TopMenuBarController(clientWindow);

        connectMenu = new JMenu("Connect");
        sessionInfoMenu = new JMenu("Session");
        interactionMenu = new JMenu("Interaction");
        helpMenu = new JMenu("Help");

        createConnectionMenuItem = new JAppMenuIconItem("Connect", ClientIcon.ADD_CONNECTION);
        disconnectMenuItem = new JAppMenuIconItem("Disconnect", ClientIcon.DISCONNECT, false);
        lastConnectionsMenuItem = new JAppMenuIconItem("Last connections", LibIcon.CHECK_BOX_LIST);
        sessionInfoMenuItem = new JAppMenuIconItem("Session info", LibIcon.STATUS_INFORMATION, false);

        takeScreenshotMenuItem = new JAppMenuIconItem("Take screenshot", ClientIcon.TAKE_SNAPSHOT, false);

        aboutMenuItem = new JAppMenuIconItem("About", LibIcon.HELP_TABLE_OF_CONTENTS);
        licenseMenuItem = new JAppMenuIconItem("License", LibIcon.CODE_INFORMATION_RULE);

        initObservables();

        connectMenuItems = new JAppMenuIconItem[]{
            createConnectionMenuItem,
            disconnectMenuItem,
            lastConnectionsMenuItem,
        };
        sessionInfoMenuItems = new JAppMenuIconItem[]{
            sessionInfoMenuItem,
        };
        interactionMenuItems = new JAppMenuIconItem[]{
            takeScreenshotMenuItem,
        };
        helpMenuItems = new JAppMenuIconItem[]{
            aboutMenuItem,
            licenseMenuItem
        };

        createConnectionMenuItem.addActionListener(e -> controller.openMakeConnectionWindow());
        disconnectMenuItem.addActionListener(e -> controller.disconnectFromSession());
        lastConnectionsMenuItem.addActionListener(e -> controller.openLastConnectionsWindow());

        sessionInfoMenuItem.addActionListener(e -> controller.openSessionInfoWindow());

        takeScreenshotMenuItem.addActionListener(e -> controller.takeScreenshot());

        aboutMenuItem.addActionListener(e -> controller.openAboutProgramSection());
        licenseMenuItem.addActionListener(e -> controller.openLicenseSection());

        addMenuItems(connectMenu, connectMenuItems);
        addMenuItems(sessionInfoMenu, sessionInfoMenuItems);
        addMenuItems(interactionMenu, interactionMenuItems);
        addMenuItems(helpMenu, helpMenuItems);

        add(connectMenu);
        add(sessionInfoMenu);
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
            sessionInfoMenuItem.setEnabled(isConnected);
        });
    }
}
