/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiWindowAdapter extends WindowAdapter {
    private final AbstractFrame frame;

    public GuiWindowAdapter(AbstractFrame frame) {
        this.frame = frame;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        final int result = JOptionPane
            .showConfirmDialog(frame, "Are you sure to close app?", "Please confirm", JOptionPane.YES_NO_OPTION);
        if (result == 0) {
            System.exit(0);
        }
    }
}
