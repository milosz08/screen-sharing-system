/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host;

import pl.polsl.screensharing.host.gui.HostWindow;
import pl.polsl.screensharing.lib.gui.AbstractFrame;
import pl.polsl.screensharing.lib.gui.GuiConfig;

import java.awt.*;

public class HostMain {
    public static void main(String[] args) {
        GuiConfig.prepareForMacos();
        EventQueue.invokeLater(() -> {
            final AbstractFrame window = new HostWindow();
            window.guiInit();
        });
    }
}
