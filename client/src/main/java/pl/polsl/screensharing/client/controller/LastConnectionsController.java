/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import pl.polsl.screensharing.client.ClientState;
import pl.polsl.screensharing.client.dto.LastConnectionRowDto;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.dialog.LastConnectionsWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LastConnectionsController implements IConnectController {
    private final LastConnectionsWindow lastConnectionsWindow;
    private final ClientState state;

    public LastConnectionsController(ClientWindow clientWindow, LastConnectionsWindow lastConnectionsWindow) {
        this.lastConnectionsWindow = lastConnectionsWindow;
        this.state = clientWindow.getCurrentState();
    }

    @Override
    public void estabilishedConnection() {
        final String ip = getTableValue(0);
        final String port = getTableValue(1);

        System.out.printf("connecting... %s:%s%n", ip, port);
    }

    public void removeSelectedRow() {
        final JTable table = lastConnectionsWindow.getTable();

        final String ip = getTableValue(0);
        final String port = getTableValue(1);

        final int result = JOptionPane.showConfirmDialog(lastConnectionsWindow,
            String.format("Are you sure to remove saved connection: %s:%s?", ip, port),
            "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
            state.removeConnectionByIndex(table.getSelectedRow());
        }
    }

    public void removeAllRows() {
        final JTable table = lastConnectionsWindow.getTable();

        final int result = JOptionPane.showConfirmDialog(lastConnectionsWindow,
            "Are you sure to remove all saved connections?",
            "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            for (int i = table.getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) table.getModel()).removeRow(i);
            }
            state.removeAllSavedConnections();
        }

    }

    public void updateLastConnectionsData(PropertyChangeEvent evt) {
        final List<LastConnectionRowDto> rows = new ArrayList<>();
        final JTable table = lastConnectionsWindow.getTable();

        if (!Objects.equals("tableCellEditor", evt.getPropertyName())) {
            return;
        }
        for (int i = 0; i < table.getRowCount(); i++) {
            final String ip = (String) table.getValueAt(i, 0);
            final int port = Integer.parseInt((String) table.getValueAt(i, 1));
            final String description = (String) table.getValueAt(i, 2);
            rows.add(new LastConnectionRowDto(ip, port, description));
        }
        state.copyAndPushLastSavedConnections(rows);
    }

    public void markupSelectedRow() {
        final JTable table = lastConnectionsWindow.getTable();
        setButtonsActive(table.getSelectedRow() != -1);
    }

    private void setButtonsActive(boolean isActive) {
        lastConnectionsWindow.getEstabilishedConnButton().setEnabled(isActive);
        lastConnectionsWindow.getRemoveRowButton().setEnabled(isActive);
    }

    private String getTableValue(int col) {
        final JTable table = lastConnectionsWindow.getTable();
        return table.getValueAt(table.getSelectedRow(), col).toString();
    }
}
