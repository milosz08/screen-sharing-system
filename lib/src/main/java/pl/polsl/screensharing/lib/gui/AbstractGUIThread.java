/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.lib.UnoperableException;
import pl.polsl.screensharing.lib.Utils;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractGUIThread<T> implements Runnable {
    private final T state;

    @Override
    public void run() {
        log.info("Starting GUI thread.");
        try {
            createThreadSaveRootFrame(state);
            log.info("Initialized application GUI.");
        } catch (UnoperableException ex) {
            log.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void init() {
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
            Utils.generateThreadUsagePerTick();
            SwingUtilities.invokeLater(this);
        } catch (UnsupportedLookAndFeelException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected abstract void createThreadSaveRootFrame(T state);
}
