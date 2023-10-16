/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.components;

import pl.polsl.screensharing.lib.gui.input.SimpleDocumentFilter;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

public class JAppTextField extends JTextField {
    private int maxCharacters;
    private String regex;

    public JAppTextField(String text, int columns) {
        super(text, columns);
    }

    public JAppTextField(int columns, int maxCharacters, String regex) {
        super(columns);
        this.maxCharacters = maxCharacters;
        this.regex = regex;
        setDocumentValidator();
    }

    public JAppTextField(String text, int columns, int maxCharacters, String regex) {
        super(text, columns);
        this.maxCharacters = maxCharacters;
        this.regex = regex;
        setDocumentValidator();
    }

    private void setDocumentValidator() {
        ((AbstractDocument) getDocument()).setDocumentFilter(new SimpleDocumentFilter(maxCharacters, regex));
    }
}
