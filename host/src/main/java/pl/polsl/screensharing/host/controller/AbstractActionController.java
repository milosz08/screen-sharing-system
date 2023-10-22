/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.dialog.ConnectionSettingsWindow;

import javax.swing.*;

abstract class AbstractActionController {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractActionController.class);

    protected final HostWindow hostWindow;

    AbstractActionController(HostWindow hostWindow) {
        this.hostWindow = hostWindow;
    }

    public void openMakeConnectionWindow() {
        final ConnectionSettingsWindow window = hostWindow.getConnectionSettingsWindow();
        window.setVisible(true);
    }
}
