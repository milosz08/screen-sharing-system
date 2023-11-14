/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host;

import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.gui.AbstractRootFrame;
import pl.polsl.screensharing.lib.gui.GuiConfig;

import javax.swing.*;

public class HostMain {
    public static void main(String[] args) {
        GuiConfig.setDefaultLayout();

        final Thread thread = new Thread(new StartNet());

        final HostState state = new HostState();
        SwingUtilities.invokeLater(() -> {
            final AbstractRootFrame window = new HostWindow(state);
            window.guiInitAndShow();
        });
    }
}
