/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client;

import pl.polsl.screensharing.client.gui.ClientWindow;
import pl.polsl.screensharing.lib.gui.AbstractFrame;
import pl.polsl.screensharing.lib.gui.GuiConfig;

import java.awt.*;

public class ClientMain {
    public static void main(String[] args) {
        GuiConfig.prepareForMacos();
        EventQueue.invokeLater(() -> {
            final AbstractFrame window = new ClientWindow();
            window.guiInit();
        });
    }
}
