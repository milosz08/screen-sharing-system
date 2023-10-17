/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view;

import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractRootFrame;

import javax.swing.*;

public class ClientWindow extends AbstractRootFrame {
    private final TopMenuBar topMenuBar;

    private final EstablishedConnectionWindow establishedConnectionWindow;
    private final LastConnectionsWindow lastConnectionsWindow;

    public ClientWindow() {
        super(AppType.CLIENT, ClientWindow.class);
        this.establishedConnectionWindow = new EstablishedConnectionWindow(this);
        this.lastConnectionsWindow = new LastConnectionsWindow(this);
        this.topMenuBar = new TopMenuBar(this);
    }

    @Override
    protected void extendsFrame(JFrame frame, JPanel rootPanel) {
        frame.setJMenuBar(topMenuBar);
    }

    public EstablishedConnectionWindow getEstablishedConnectionWindow() {
        return establishedConnectionWindow;
    }

    public LastConnectionsWindow getLastConnectionsWindow() {
        return lastConnectionsWindow;
    }
}
