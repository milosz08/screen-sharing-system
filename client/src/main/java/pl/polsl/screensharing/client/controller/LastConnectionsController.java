/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import pl.polsl.screensharing.client.view.LastConnectionsWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LastConnectionsController implements IConnectController {
    private final LastConnectionsWindow window;

    public LastConnectionsController(LastConnectionsWindow window) {
        this.window = window;
    }

    @Override
    public void estabilishedConnection() {
        final String ip = getTableValue(0);
        final String port = getTableValue(1);

        System.out.printf("connecting... %s:%s%n", ip, port);
    }

    public void removeSelectedRow() {
        final JTable table = window.getTable();

        final String ip = getTableValue(0);
        final String port = getTableValue(1);

        final int result = JOptionPane.showConfirmDialog(window,
            String.format("Are you sure to remove saved connection: %s:%s?", ip, port),
            "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
        }
    }

    public void removeAllRows() {
        final JTable table = window.getTable();

        final int result = JOptionPane.showConfirmDialog(window, "Are you sure to remove all saved connections?",
            "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            for (int i = table.getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) table.getModel()).removeRow(i);
            }
        }
    }

    public void markupSelectedRow() {
        final JTable table = window.getTable();
        setButtonsActive(table.getSelectedRow() != -1);
    }

    private void setButtonsActive(boolean isActive) {
        window.getEstabilishedConnButton().setEnabled(isActive);
        window.getRemoveRowButton().setEnabled(isActive);
    }

    private String getTableValue(int col) {
        final JTable table = window.getTable();
        return table.getValueAt(table.getSelectedRow(), col).toString();
    }
}
