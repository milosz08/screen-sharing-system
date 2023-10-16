/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.gui;

import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractRootFrame;

import javax.swing.*;

public class ClientWindow extends AbstractRootFrame {
    private final TopMenuBar topMenuBar;

    public ClientWindow() {
        super(AppType.CLIENT, ClientWindow.class);
        this.topMenuBar = new TopMenuBar(this);
    }

    @Override
    protected void extendsFrame(JFrame frame, JPanel rootPanel) {
        frame.setJMenuBar(topMenuBar);
    }
}
