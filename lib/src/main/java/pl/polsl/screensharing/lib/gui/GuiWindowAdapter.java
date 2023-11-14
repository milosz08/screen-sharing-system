/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui;

import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@RequiredArgsConstructor
public class GuiWindowAdapter extends WindowAdapter {
    private final AbstractRootFrame frame;

    @Override
    public void windowClosing(WindowEvent e) {
        final int result = JOptionPane.showConfirmDialog(frame,
            "Are you sure to close app?", "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
