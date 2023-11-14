/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractGUIThread implements Runnable {
    private final AbstractRootFrame frame;

    @Override
    public void run() {
        log.info("Starting GUI thread.");
        createThreadSaveRootFrame(frame);
        log.info("Initialized application GUI.");
    }

    public void init() {
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
            SwingUtilities.invokeLater(this);
        } catch (UnsupportedLookAndFeelException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected abstract void createThreadSaveRootFrame(AbstractRootFrame frame);
}
