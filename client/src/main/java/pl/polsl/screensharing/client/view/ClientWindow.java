/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view;

import pl.polsl.screensharing.client.view.dialog.AboutDialogWindow;
import pl.polsl.screensharing.client.view.dialog.EstablishedConnectionWindow;
import pl.polsl.screensharing.client.view.dialog.LastConnectionsWindow;
import pl.polsl.screensharing.client.view.dialog.LicenseDialogWindow;
import pl.polsl.screensharing.client.view.fragment.TopMenuBar;
import pl.polsl.screensharing.client.view.fragment.TopToolbar;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractRootFrame;

import javax.swing.*;
import java.awt.*;

public class ClientWindow extends AbstractRootFrame {
    private final TopMenuBar topMenuBar;
    private final TopToolbar topToolbar;

    private final EstablishedConnectionWindow establishedConnectionWindow;
    private final LastConnectionsWindow lastConnectionsWindow;
    private final AboutDialogWindow aboutDialogWindow;
    private final LicenseDialogWindow licenseDialogWindow;

    public ClientWindow() {
        super(AppType.CLIENT, ClientWindow.class);

        this.establishedConnectionWindow = new EstablishedConnectionWindow(this);
        this.lastConnectionsWindow = new LastConnectionsWindow(this);
        this.aboutDialogWindow = new AboutDialogWindow(this);
        this.licenseDialogWindow = new LicenseDialogWindow(this);

        this.topMenuBar = new TopMenuBar(this);
        this.topToolbar = new TopToolbar(this);
    }

    @Override
    protected void extendsFrame(JFrame frame, JPanel rootPanel) {
        frame.setJMenuBar(topMenuBar);
        frame.add(topToolbar, BorderLayout.NORTH);
    }

    public EstablishedConnectionWindow getEstablishedConnectionWindow() {
        return establishedConnectionWindow;
    }

    public LastConnectionsWindow getLastConnectionsWindow() {
        return lastConnectionsWindow;
    }

    public AboutDialogWindow getAboutDialogWindow() {
        return aboutDialogWindow;
    }

    public LicenseDialogWindow getLicenseDialogWindow() {
        return licenseDialogWindow;
    }
}
