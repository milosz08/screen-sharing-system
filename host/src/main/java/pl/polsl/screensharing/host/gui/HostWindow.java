/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.gui;

import pl.polsl.screensharing.lib.gui.AbstractFrame;

public class HostWindow extends AbstractFrame {
    private static final String TITLE = "HOST - Screen sharing";
    private static final String ICON_PATCH = "assets/host-icon.png";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public HostWindow() {
        super(TITLE, WIDTH, HEIGHT, ICON_PATCH, HostWindow.class);
    }
}
