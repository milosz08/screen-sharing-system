/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host;

import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.gui.AbstractRootFrame;
import pl.polsl.screensharing.lib.gui.GuiConfig;

import javax.swing.*;

public class HostMain {
    public static void main(String[] args) {
        GuiConfig.prepareForMacos();
        SwingUtilities.invokeLater(() -> {
            final AbstractRootFrame window = new HostWindow();
            window.guiInitAndShow();
        });
    }
}
