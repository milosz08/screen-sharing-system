/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.component;

import pl.polsl.screensharing.lib.gui.input.SimpleDocumentFilter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;

public class JAppTextField extends JTextField {
    private final int maxCharacters;
    private final String regex;

    public JAppTextField(int columns, int maxCharacters, String regex) {
        super(columns);
        this.maxCharacters = maxCharacters;
        this.regex = regex;
        setComponentProperties();
    }

    public JAppTextField(String text, int columns, int maxCharacters, String regex) {
        super(text, columns);
        this.maxCharacters = maxCharacters;
        this.regex = regex;
        setComponentProperties();
    }

    private void setComponentProperties() {
        setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        setDocumentValidator();
    }

    private void setDocumentValidator() {
        ((AbstractDocument) getDocument()).setDocumentFilter(new SimpleDocumentFilter(maxCharacters, regex));
    }
}
