/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.gui.AbstractRootFrame;
import pl.polsl.screensharing.lib.gui.GuiConfig;

import javax.swing.*;

public class ClientMain {
    private static final Logger LOG = LoggerFactory.getLogger(ClientMain.class);

    public static void main(String[] args) {
        GuiConfig.setDefaultLayout();
        final ClientState clientState = new ClientState();
        SwingUtilities.invokeLater(() -> {
            LOG.info("Starting GUI thread.");
            final AbstractRootFrame window = new ClientWindow(clientState);
            window.guiInitAndShow();
            LOG.info("Initialized application GUI.");
        });
    }
}
