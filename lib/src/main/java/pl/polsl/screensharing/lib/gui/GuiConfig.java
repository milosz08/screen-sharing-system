/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui;

public class GuiConfig {
    private GuiConfig() {
    }

    public static void prepareForMacos() {
        // set menu for macos top floating bar
        if (System.getProperty("os.name").contains("Mac")) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }
    }
}
