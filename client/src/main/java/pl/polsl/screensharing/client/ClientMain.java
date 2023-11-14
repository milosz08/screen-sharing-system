/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.client.net.StartNet;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.gui.AbstractRootFrame;
import pl.polsl.screensharing.lib.gui.GuiConfig;

import javax.swing.*;

@Slf4j
public class ClientMain {
    public static void main(String[] args) {
        GuiConfig.setDefaultLayout();
        final ClientState clientState = new ClientState();
        SwingUtilities.invokeLater(() -> {
            log.info("Starting GUI thread.");
            final AbstractRootFrame window = new ClientWindow(clientState);
            window.guiInitAndShow();
            thread.start();

            log.info("Initialized application GUI.");
        });
    }
}
