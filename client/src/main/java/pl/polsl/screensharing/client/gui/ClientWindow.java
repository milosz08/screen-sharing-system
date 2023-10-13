/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.gui;

import pl.polsl.screensharing.lib.gui.AbstractFrame;

public class ClientWindow extends AbstractFrame {
    private static final String TITLE = "CLIENT - Screen sharing";
    private static final String ICON_PATH = "assets/client-icon.png";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public ClientWindow() {
        super(TITLE, WIDTH, HEIGHT, ICON_PATH, ClientWindow.class);
    }
}
