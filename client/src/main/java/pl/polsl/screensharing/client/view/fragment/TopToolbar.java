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
    private final JAppIconButton takeScreenshotButton;

    private final TopToolbarController controller;

    public TopToolbar(ClientWindow clientWindow) {
        this.clientState = clientWindow.getClientState();
        this.controller = new TopToolbarController(clientWindow);

        this.createConnectionButton = new JAppIconButton("Connect", ClientIcon.ADD_CONNECTION, true);
        this.disconnectButton = new JAppIconButton("Disconnect", ClientIcon.DISCONNECT, true, false);
        this.lastConnectionsButton = new JAppIconButton("Last connections", LibIcon.CHECK_BOX_LIST, true);

        this.takeScreenshotButton = new JAppIconButton("Take screenshot", ClientIcon.TAKE_SNAPSHOT, true, false);

        initObservables();

        this.createConnectionButton.addActionListener(e -> controller.openMakeConnectionWindow());
        this.disconnectButton.addActionListener(e -> controller.disconnectFromSession());
        this.lastConnectionsButton.addActionListener(e -> controller.openLastConnectionsWindow());

        this.takeScreenshotButton.addActionListener(e -> controller.takeScreenshot());

        addButtonWithSeparation(createConnectionButton);
        addButtonWithSeparation(disconnectButton);
        addButtonWithSeparation(lastConnectionsButton);
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
        });
    }
}
