/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.input;

import lombok.RequiredArgsConstructor;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class SimpleDocumentFilter extends DocumentFilter {
    private final int maxCharacters;
    private final String regex;

    public SimpleDocumentFilter(int maxCharacters) {
        this.maxCharacters = maxCharacters;
        regex = null;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
        if (isValidRegex(newText) && (fb.getDocument().getLength() + string.length() <= maxCharacters)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        String newText = fb.getDocument().getText(0, fb.getDocument().getLength() - length) + text;
        if (isValidRegex(newText) && (fb.getDocument().getLength() + text.length() - length <= maxCharacters)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private boolean isValidRegex(String text) {
        if (regex == null) {
            return true;
        }
        return Pattern.matches(regex, text);
    }
}
