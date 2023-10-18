/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.input;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.PlainDocument;

public class AppCellEditor extends DefaultCellEditor {
    private final JTextField textField;

    public AppCellEditor(int charactersNumber, String regex) {
        super(new JTextField());
        this.textField = (JTextField) getComponent();
        this.textField.setDocument(new PlainDocument());
        ((AbstractDocument) this.textField.getDocument())
            .setDocumentFilter(new SimpleDocumentFilter(charactersNumber, regex));
    }

    public static void setCellEditor(int col, int charactersNumber, String regex, JTable table) {
        table.getColumnModel().getColumn(col).setCellEditor(new AppCellEditor(charactersNumber, regex));
    }

    public static void setCellEditor(int col, int charactersNumber, JTable table) {
        setCellEditor(col, charactersNumber, null, table);
    }
}
