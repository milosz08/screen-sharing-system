/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.gui.AbstractRootFrame;
import pl.polsl.screensharing.lib.gui.GuiConfig;

import javax.swing.*;

@Slf4j
public class HostMain {
    public static void main(String[] args) {
        GuiConfig.setDefaultLayout();
        final HostState state = new HostState();
        SwingUtilities.invokeLater(() -> {
            log.info("Starting GUI thread.");
            final AbstractRootFrame window = new HostWindow(state);
            window.guiInitAndShow();
            log.info("Initialized application GUI.");
        });
    }
}
