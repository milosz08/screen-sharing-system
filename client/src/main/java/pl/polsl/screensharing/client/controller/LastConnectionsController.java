/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.client.dto.ConnDetailsDto;
import pl.polsl.screensharing.client.dto.SavedConnDetailsDto;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.dialog.LastConnectionsWindow;
import pl.polsl.screensharing.client.view.popup.PasswordPopup;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.beans.PropertyChangeEvent;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

@Slf4j
public class LastConnectionsController extends AbstractPopupDialogController {
    private final LastConnectionsWindow lastConnectionsWindow;

    public LastConnectionsController(ClientWindow clientWindow, LastConnectionsWindow lastConnectionsWindow) {
        super(clientWindow, lastConnectionsWindow);
        this.lastConnectionsWindow = lastConnectionsWindow;
    }

    @Override
    protected ConnDetailsDto createConnectionParameters() {
        final PasswordPopup passwordPopup = new PasswordPopup(lastConnectionsWindow);
        final String password = passwordPopup.showPopupAndWaitForInput();
        if (password == null) {
            return null;
        }
        return ConnDetailsDto.builder()
            .ipAddress(getTableValue(0))
            .port(Integer.parseInt(getTableValue(1)))
            .username(getTableValue(2))
            .password(password)
            .build();
    }

    @Override
    protected void onSuccessConnect(ConnDetailsDto detailsDto) {
    }

    public void removeSelectedRow() {
        final JTable table = lastConnectionsWindow.getTable();
        final ClientState state = clientWindow.getClientState();

        final int selectedRow = table.getSelectedRow();

        final String ip = getTableValue(0);
        final String port = getTableValue(1);

        final int result = JOptionPane.showConfirmDialog(lastConnectionsWindow,
            String.format("Are you sure to remove saved connection: %s:%s?", ip, port),
            "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
            state.removeConnDetailsByIndex(selectedRow);
        }
    }

    public void removeAllRows() {
        final JTable table = lastConnectionsWindow.getTable();
        final ClientState state = clientWindow.getClientState();

        final int result = JOptionPane.showConfirmDialog(lastConnectionsWindow,
            "Are you sure to remove all saved connections?",
            "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result != JOptionPane.YES_OPTION) {
            return;
        }
        for (int i = table.getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) table.getModel()).removeRow(i);
        }
        state.removeAllSavedConnDetails();
    }

    public void updateLastConnectionsData(PropertyChangeEvent evt) {
        final SortedSet<SavedConnDetailsDto> rows = new TreeSet<>();
        final ClientState state = clientWindow.getClientState();
        final JTable table = lastConnectionsWindow.getTable();

        if (!Objects.equals("tableCellEditor", evt.getPropertyName())) {
            return;
        }
        for (int i = 0; i < table.getRowCount(); i++) {
            final SavedConnDetailsDto detailsDto = SavedConnDetailsDto.builder()
                .id(i)
                .ipAddress((String) table.getValueAt(i, 0))
                .port(Integer.parseInt(String.valueOf(table.getValueAt(i, 1))))
                .username((String) table.getValueAt(i, 2))
                .description((String) table.getValueAt(i, 3))
                .build();
            rows.add(detailsDto);
        }
        state.copyAndPushSavedConnDetails(rows);
    }

    public void markupSelectedRow() {
        final JTable table = lastConnectionsWindow.getTable();
        final boolean isActive = table.getSelectedRow() != -1;

        lastConnectionsWindow.getConnectButton().setEnabled(isActive);
        lastConnectionsWindow.getRemoveRowButton().setEnabled(isActive);
    }

    private String getTableValue(int col) {
        final JTable table = lastConnectionsWindow.getTable();
        return table.getValueAt(table.getSelectedRow(), col).toString();
    }
}
