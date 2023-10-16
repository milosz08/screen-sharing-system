/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.input;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SimpleDocumentListener implements DocumentListener {
    private final Runnable executeOnUpdate;

    public SimpleDocumentListener(Runnable executeOnUpdate) {
        this.executeOnUpdate = executeOnUpdate;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        executeOnUpdate.run();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        executeOnUpdate.run();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        executeOnUpdate.run();
    }
}
