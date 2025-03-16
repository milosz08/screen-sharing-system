package pl.polsl.screensharing.lib.gui.input;

import lombok.RequiredArgsConstructor;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@RequiredArgsConstructor
public class SimpleDocumentListener implements DocumentListener {
    private final Runnable executeOnUpdate;

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
