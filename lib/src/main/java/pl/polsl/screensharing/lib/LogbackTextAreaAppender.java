/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import pl.polsl.screensharing.lib.gui.fragment.JAppTabbedLogsPanel;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

public class LogbackTextAreaAppender extends AppenderBase<ILoggingEvent> {
    private PatternLayout patternLayout;

    private static final int LIMIT = 1000;
    private static final int CUT_RANGE = 200;

    @Override
    public void start() {
        patternLayout = new PatternLayout();
        patternLayout.setContext(getContext());
        patternLayout.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} > [%-5p] {%thread} : %m%n");
        patternLayout.start();
        super.start();
    }

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        SwingUtilities.invokeLater(() -> {
            final String formattedMsg = patternLayout.doLayout(iLoggingEvent);
            final JTextArea textArea = JAppTabbedLogsPanel.textArea;
            if (textArea == null) {
                return;
            }
            try {
                final Document document = textArea.getDocument();
                if (document.getDefaultRootElement().getElementCount() > LIMIT) {
                    replaceRange(textArea, getLineEndOffset(textArea));
                }
                textArea.append(formattedMsg);
            } catch (RuntimeException ignored) {
            }
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }

    private void replaceRange(JTextArea textArea, int end) {
        final Document doc = textArea.getDocument();
        if (doc == null) {
            return;
        }
        try {
            if (doc instanceof AbstractDocument) {
                ((AbstractDocument) doc).replace(0, end, null, null);
            } else {
                doc.remove(0, end);
                doc.insertString(0, null, null);
            }
        } catch (BadLocationException e) {
            throw new RuntimeException();
        }
    }

    private int getLineEndOffset(JTextArea textArea) {
        int lineCount = textArea.getDocument().getDefaultRootElement().getElementCount();
        if (CUT_RANGE >= lineCount) {
            throw new RuntimeException();
        }
        final Element map = textArea.getDocument().getDefaultRootElement();
        final Element lineElem = map.getElement(CUT_RANGE);
        final int endOffset = lineElem.getEndOffset();
        return (CUT_RANGE == lineCount - 1) ? (endOffset - 1) : endOffset;
    }
}
