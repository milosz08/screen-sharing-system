/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client;

import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.gui.AbstractRootFrame;
import pl.polsl.screensharing.lib.gui.GuiConfig;

import javax.swing.*;

public class ClientMain {
    public static void main(String[] args) {
        GuiConfig.prepareForMacos();
        SwingUtilities.invokeLater(() -> {
            final AbstractRootFrame window = new ClientWindow();
            window.guiInitAndShow();
        });
    }
}
