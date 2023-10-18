/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.fragment;

import pl.polsl.screensharing.client.controller.TopToolbarController;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.AppIcon;
import pl.polsl.screensharing.lib.gui.component.JAppIconButton;

import javax.swing.*;

public class TopToolbar extends JToolBar {
    private final JAppIconButton createConnectionButton;
    private final JAppIconButton disconnectButton;
    private final JAppIconButton lastConnectionsButton;

    private final TopToolbarController controller;

    public TopToolbar(ClientWindow clientWindow) {
        this.controller = new TopToolbarController(clientWindow);

        this.createConnectionButton = new JAppIconButton("Connect", AppIcon.ADD_CONNECTION, true);
        this.disconnectButton = new JAppIconButton("Disconnect", AppIcon.DISCONNECT, true, false);
        this.lastConnectionsButton = new JAppIconButton("Last connections", AppIcon.CHECK_BOX_LIST, true);

        this.createConnectionButton.addActionListener(e -> controller.openMakeConnectionWindow());
        this.disconnectButton.addActionListener(e -> controller.disconnectFromSession());
        this.lastConnectionsButton.addActionListener(e -> controller.openLastConnectionsWindow());

        addButtonWithSeparation(createConnectionButton);
        addButtonWithSeparation(disconnectButton);
        addButtonWithSeparation(lastConnectionsButton);

        setFloatable(false);
    }

    private void addButtonWithSeparation(JAppIconButton button) {
        add(button);
        add(Box.createHorizontalStrut(5));
    }

    public void setConnectionButtonsState(boolean onIsConnect) {
        disconnectButton.setEnabled(onIsConnect);
        createConnectionButton.setEnabled(!onIsConnect);
        lastConnectionsButton.setEnabled(!onIsConnect);
    }
}
