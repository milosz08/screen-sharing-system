/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.polsl.screensharing.client.dto.ConnectionDetailsDto;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.dialog.EstablishedConnectionWindow;
import pl.polsl.screensharing.client.view.dialog.LastConnectionsWindow;
import pl.polsl.screensharing.lib.gui.component.JAppPasswordTextField;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;

public class EstabilishedConnectionController implements IConnectController {
    private static final Logger LOG = LoggerFactory.getLogger(EstabilishedConnectionController.class);

    private final EstablishedConnectionWindow connectionWindow;
    private final ClientWindow clientWindow;
    private final ClientState state;

    public EstabilishedConnectionController(
        ClientWindow clientWindow,
        EstablishedConnectionWindow establishedConnectionWindow
    ) {
        this.connectionWindow = establishedConnectionWindow;
        this.clientWindow = clientWindow;
        this.state = clientWindow.getCurrentState();
    }

    @Override
    public void estabilishedConnection() {
        final ConnectionDetailsDto dto = createConnectionDetailsDto();
        final String password = new String(connectionWindow.getPasswordTextField().getPassword());
        final boolean isSaving = connectionWindow.getAddToListCheckbox().isSelected();

        final ConnectionDetailsDto connDetails = state.getConnectionDetails();
        connDetails.setConnectionDetails(dto);
        if (isSaving) {
            addNewConnectionInSaveList(dto);
        }
        state.setConnectionEstabilished(true);
        clientWindow.getTopMenuBar().setConnectionButtonsState(true);
        clientWindow.getTopToolbar().setConnectionButtonsState(true);

        // TODO: connect to host

        LOG.info("Estabilished connection: {}", connDetails);
        closeWindow();
    }

    public void closeWindow() {
        connectionWindow.closeWindow();
        connectionWindow.dispose();
    }

    private void addNewConnectionInSaveList(ConnectionDetailsDto dto) {
        final String host = dto.getHostAddress();
        final int port = dto.getIpv4().getPort();
        final String username = dto.getUsername();
        final String description = dto.getDescription();
        final LastConnectionsWindow lastConnectionsWindow = clientWindow.getLastConnectionsWindow();
        final DefaultTableModel model = (DefaultTableModel) lastConnectionsWindow.getTable().getModel();
        final boolean alreadyExist = state.addNewSavedConnection(host, port, username, description);
        if (!alreadyExist) {
            model.addRow(new Object[]{ host, port, username, description });
        }
    }

    private ConnectionDetailsDto createConnectionDetailsDto() {
        final String host = connectionWindow.getIpAddressTextField().getText();
        final String port = connectionWindow.getPortTextField().getText();
        final String username = connectionWindow.getUsernameTextField().getText();
        final String description = connectionWindow.getDescriptionTextArea().getText();
        return new ConnectionDetailsDto(host, Integer.parseInt(port), username, description);
    }

    public void saveConnectionDetails(ActionEvent event) {
        final JButton saveDetailsButton = (JButton) event.getSource();
        saveDetailsButton.setEnabled(false);
        state.persistConnectionDetails(createConnectionDetailsDto());
        JOptionPane.showConfirmDialog(connectionWindow, "Your connection details was successfully saved.",
            "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
    }

    public void resetSaveButtonState() {
        final JButton saveDetailsButton = connectionWindow.getSaveDetailsButton();
        saveDetailsButton.setEnabled(true);
    }

    public void togglePasswordField(ActionEvent event) {
        final JAppPasswordTextField passwordField = connectionWindow.getPasswordTextField();
        final JCheckBox checkBox = (JCheckBox) event.getSource();
        passwordField.toggleVisibility(checkBox.isSelected());
    }
}
