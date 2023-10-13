/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui;

public class GuiConfig {
    private GuiConfig() {
    }

    public static void prepareForMacos() {
        if (System.getProperty("os.name").contains("Mac")) {
            // set menu for macos top floating bar
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }
    }
}
