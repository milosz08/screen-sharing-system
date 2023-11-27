/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.fragment;

import lombok.Getter;
import pl.polsl.screensharing.client.controller.TopToolbarController;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.state.ConnectionState;
import pl.polsl.screensharing.client.view.ClientIcon;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.gui.component.JAppIconButton;
import pl.polsl.screensharing.lib.gui.icon.LibIcon;

import javax.swing.*;

@Getter
public class TopToolbar extends JToolBar {
    private final ClientState clientState;

    private final JAppIconButton createConnectionButton;
    private final JAppIconButton disconnectButton;
    private final JAppIconButton lastConnectionsButton;
    private final JAppIconButton sessionInfoButton;
    private final JAppIconButton takeScreenshotButton;

    private final TopToolbarController controller;

    public TopToolbar(ClientWindow clientWindow) {
        clientState = clientWindow.getClientState();
        controller = new TopToolbarController(clientWindow);

        createConnectionButton = new JAppIconButton("Connect", ClientIcon.ADD_CONNECTION, true);
        disconnectButton = new JAppIconButton("Disconnect", ClientIcon.DISCONNECT, true, false);
        lastConnectionsButton = new JAppIconButton("Last connections", LibIcon.CHECK_BOX_LIST, true);

        sessionInfoButton = new JAppIconButton("Session info", LibIcon.STATUS_INFORMATION, true, false);

        takeScreenshotButton = new JAppIconButton("Take screenshot", ClientIcon.TAKE_SNAPSHOT, true, false);

        initObservables();

        createConnectionButton.addActionListener(e -> controller.openMakeConnectionWindow());
        disconnectButton.addActionListener(e -> controller.disconnectFromSession());
        lastConnectionsButton.addActionListener(e -> controller.openLastConnectionsWindow());

        sessionInfoButton.addActionListener(e -> controller.openSessionInfoWindow());

        takeScreenshotButton.addActionListener(e -> controller.takeScreenshot());

        addButtonWithSeparation(createConnectionButton);
        addButtonWithSeparation(disconnectButton);
        addButtonWithSeparation(lastConnectionsButton);
        addSeparator();
        addButtonWithSeparation(sessionInfoButton);
        addSeparator();
        addButtonWithSeparation(takeScreenshotButton);

        setFloatable(false);
    }

    private void addButtonWithSeparation(JAppIconButton button) {
        add(button);
        add(Box.createHorizontalStrut(5));
    }

    private void initObservables() {
        clientState.wrapAsDisposable(clientState.getConnectionState$(), state -> {
            final boolean isConnected = state.equals(ConnectionState.CONNECTED);
            createConnectionButton.setEnabled(!isConnected);
            lastConnectionsButton.setEnabled(!isConnected);
            disconnectButton.setEnabled(isConnected);
            takeScreenshotButton.setEnabled(isConnected);
            sessionInfoButton.setEnabled(isConnected);
        });
    }
}
