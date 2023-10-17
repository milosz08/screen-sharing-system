/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view;

import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractRootFrame;

import javax.swing.*;

public class HostWindow extends AbstractRootFrame {
    public HostWindow() {
        super(AppType.HOST, HostWindow.class);
    }

    @Override
    protected void extendsFrame(JFrame frame, JPanel rootPanel) {
    }
}
