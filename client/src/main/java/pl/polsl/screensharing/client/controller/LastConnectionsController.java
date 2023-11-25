/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.client.model.ConnectionDetails;
import pl.polsl.screensharing.client.model.SavedConnection;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.dialog.LastConnectionsWindow;
import pl.polsl.screensharing.client.view.popup.PasswordPopup;
import pl.polsl.screensharing.lib.InetAddr;
import pl.polsl.screensharing.lib.Utils;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.util.Objects;
import java.util.Optional;
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
    protected ConnectionDetails createConnectionParameters() {
        final PasswordPopup passwordPopup = new PasswordPopup(lastConnectionsWindow);
        final String password = passwordPopup.showPopupAndWaitForInput();
        if (password == null) {
            return null;
        }
        final InetAddr hostConnection = Utils.extractAddrDetails(getTableValue(0));
        final InetAddr clientConnection = Utils.extractAddrDetails(getTableValue(1));
        return ConnectionDetails.builder()
            .hostIpAddress(hostConnection.getIpAddress())
            .hostPort(hostConnection.getPort())
            .clientIpAddress(clientConnection.getIpAddress())
            .clientPort(clientConnection.getPort())
            .username(getTableValue(2))
            .password(password)
            .build();
    }

    @Override
    protected void onSuccessConnect(ConnectionDetails connectionDetails) {
    }

    public void removeSelectedRow() {
        final JTable table = lastConnectionsWindow.getTable();
        final ClientState clientState = clientWindow.getClientState();

        final int selectedRow = table.getSelectedRow();

        final String hostAddress = getTableValue(0);
        final String clientAddress = getTableValue(1);

        final int result = JOptionPane.showConfirmDialog(lastConnectionsWindow,
            String.format("Are you sure to remove saved connection: h: %s, c: %s?", hostAddress, clientAddress),
            "Please confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result != JOptionPane.YES_OPTION) {
            return;
        }
        final SortedSet<SavedConnection> lastSavedConnections = clientState.getLastEmittedSavedConnections();
        if (selectedRow < 0 || selectedRow >= lastSavedConnections.size()) {
            return;
        }
        final Optional<SavedConnection> removedOptional = lastSavedConnections.stream()
            .filter(details -> details.getId() == selectedRow)
            .findFirst();
        if (!removedOptional.isPresent()) {
            return;
        }
        final SavedConnection removed = removedOptional.get();
        lastSavedConnections.remove(removed);

        int i = 0;
        for (final SavedConnection notRemovable : lastSavedConnections) {
            notRemovable.setId(i++);
        }
        log.info("Removed last connection: {}.", removed);
        log.info("Reorganized rows: {}.", lastSavedConnections);

        clientState.updateSavedConnections(lastSavedConnections);
        clientState.getPersistedStateLoader().persistSavedConnDetails();
    }

    public void removeAllRows() {
        final ClientState clientState = clientWindow.getClientState();

        final int result = JOptionPane.showConfirmDialog(lastConnectionsWindow,
            "Are you sure to remove all saved connections?",
            "Please confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result != JOptionPane.YES_OPTION) {
            return;
        }
        clientState.updateSavedConnections(new TreeSet<>());
        clientState.getPersistedStateLoader().persistSavedConnDetails();
        log.info("Removed all saved last connections.");
    }

    public void updateLastConnectionsData(PropertyChangeEvent evt) {
        final SortedSet<SavedConnection> savedConnections = new TreeSet<>();
        final ClientState clientState = clientWindow.getClientState();
        final JTable table = lastConnectionsWindow.getTable();

        if (!Objects.equals("tableCellEditor", evt.getPropertyName())) {
            return;
        }
        for (int i = 0; i < table.getRowCount(); i++) {
            final InetAddr hostConnection = Utils.extractAddrDetails((String) table.getValueAt(i, 0));
            final InetAddr clientConnection = Utils.extractAddrDetails((String) table.getValueAt(i, 1));
            final SavedConnection savedConnection = SavedConnection.builder()
                .id(i)
                .hostIpAddress(hostConnection.getIpAddress())
                .hostPort(hostConnection.getPort())
                .clientIpAddress(clientConnection.getIpAddress())
                .clientPort(clientConnection.getPort())
                .username((String) table.getValueAt(i, 2))
                .description((String) table.getValueAt(i, 3))
                .build();
            savedConnections.add(savedConnection);
        }
        clientState.updateSavedConnections(new TreeSet<>());
        clientState.updateSavedConnections(savedConnections);
        clientState.getPersistedStateLoader().persistSavedConnDetails();
        log.info("Update last connections table rows. Updated table: {}.", savedConnections);
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
