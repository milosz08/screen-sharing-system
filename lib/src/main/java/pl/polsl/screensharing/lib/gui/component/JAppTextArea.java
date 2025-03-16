package pl.polsl.screensharing.lib.gui.component;

import pl.polsl.screensharing.lib.gui.input.SimpleDocumentFilter;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

public class JAppTextArea extends JTextArea {
    private final int maxCharacters;

    public JAppTextArea(int rows, int columns, int maxCharacters) {
        super(rows, columns);
        this.maxCharacters = maxCharacters;
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        setDocumentValidator();
    }

    private void setDocumentValidator() {
        ((AbstractDocument) getDocument()).setDocumentFilter(new SimpleDocumentFilter(maxCharacters));
    }

}
