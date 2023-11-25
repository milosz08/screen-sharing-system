/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pl.polsl.screensharing.client.model.ConnectionDetails;
import pl.polsl.screensharing.client.model.FastConnectionDetails;
import pl.polsl.screensharing.client.model.SavedConnection;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.dialog.ConnectWindow;
import pl.polsl.screensharing.lib.Utils;
import pl.polsl.screensharing.lib.gui.component.JAppPasswordTextField;
import pl.polsl.screensharing.lib.gui.component.JAppTextField;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.SortedSet;

@Slf4j
public class ConnectController extends AbstractPopupDialogController {
    private final ConnectWindow connectionWindow;

    public ConnectController(ClientWindow clientWindow, ConnectWindow connectWindow) {
        super(clientWindow, connectWindow);
        this.connectionWindow = connectWindow;
    }

    @Override
    protected ConnectionDetails createConnectionParameters() {
        if (connectionWindow.getHostIpAddressTextField().getText().equals(StringUtils.EMPTY) ||
            connectionWindow.getHostPortTextField().getText().equals(StringUtils.EMPTY) ||
            connectionWindow.getClientIpAddressTextField().getText().equals(StringUtils.EMPTY) ||
            connectionWindow.getClientPortTextField().getText().equals(StringUtils.EMPTY) ||
            connectionWindow.getUsernameTextField().getText().equals(StringUtils.EMPTY)
        ) {
            JOptionPane.showMessageDialog(null, "Fill all necesarry fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return ConnectionDetails.builder()
            .hostIpAddress(connectionWindow.getHostIpAddressTextField().getText())
            .hostPort(Integer.parseInt(connectionWindow.getHostPortTextField().getText()))
            .clientIpAddress(connectionWindow.getClientIpAddressTextField().getText())
            .clientPort(Integer.parseInt(connectionWindow.getClientPortTextField().getText()))
            .username(connectionWindow.getUsernameTextField().getText())
            .password(new String(connectionWindow.getPasswordTextField().getPassword()))
            .build();
    }

    @Override
    protected void onSuccessConnect(ConnectionDetails connectionDetails) {
        final ClientState clientState = clientWindow.getClientState();
        final boolean isSaving = connectionWindow.getAddToListCheckbox().isSelected();
        if (!isSaving) {
            return;
        }
        final SavedConnection savedConnection = SavedConnection.builder()
            .hostIpAddress(connectionDetails.getHostIpAddress())
            .hostPort(connectionDetails.getHostPort())
            .clientIpAddress(connectionDetails.getClientIpAddress())
            .clientPort(connectionDetails.getClientPort())
            .username(connectionDetails.getUsername())
            .description(connectionWindow.getDescriptionTextArea().getText())
            .build();

        final SortedSet<SavedConnection> lastEmittedConnections = clientState.getLastEmittedSavedConnections();
        savedConnection.setId(lastEmittedConnections.size());

        final boolean isNew = lastEmittedConnections.stream()
            .noneMatch(connection -> connection.equals(savedConnection));

        if (!isNew) {
            log.info("Saved connection {} already exist. Skipping.", connectionDetails);
            return;
        }
        lastEmittedConnections.add(savedConnection);
        clientState.updateSavedConnections(lastEmittedConnections);
        clientState.getPersistedStateLoader().persistSavedConnDetails();
        log.info("Add new saved connection: {}.", savedConnection);
    }

    public void saveConnectionDetails(ActionEvent event) {
        final ClientState state = clientWindow.getClientState();
        final JButton saveDetailsButton = (JButton) event.getSource();

        final FastConnectionDetails savedConnDetails = FastConnectionDetails.builder()
            .hostIpAddress(connectionWindow.getHostIpAddressTextField().getText())
            .hostPort(Integer.parseInt(connectionWindow.getHostPortTextField().getText()))
            .clientIpAddress(connectionWindow.getClientIpAddressTextField().getText())
            .isMachineIpAddress(connectionWindow.getIsClientMachineIpAddressCheckbox().isSelected())
            .clientPort(Integer.parseInt(connectionWindow.getClientPortTextField().getText()))
            .isRandomPort(connectionWindow.getIsClientRandomPortCheckbox().isSelected())
            .username(connectionWindow.getUsernameTextField().getText())
            .description(connectionWindow.getDescriptionTextArea().getText())
            .build();

        state.updateFastConnectionDetails(savedConnDetails);
        state.getPersistedStateLoader().persistFastConnDetails();
        log.info("Updated fast connection details: {}", savedConnDetails);

        saveDetailsButton.setEnabled(false);

        JOptionPane.showConfirmDialog(connectionWindow, "Your connection details was successfully saved.",
            "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
    }

    public void resetSaveButtonState() {
        final JButton saveDetailsButton = connectionWindow.getSaveDetailsButton();
        saveDetailsButton.setEnabled(true);
    }

    public void togglePasswordField(ActionEvent event) {
        final JCheckBox checkBox = (JCheckBox) event.getSource();
        final JAppPasswordTextField passwordField = connectionWindow.getPasswordTextField();
        passwordField.toggleVisibility(checkBox.isSelected());
    }

    public void toggleMachineIpField(ActionEvent event) {
        final JCheckBox checkBox = (JCheckBox) event.getSource();
        final JAppTextField clientIpField = connectionWindow.getClientIpAddressTextField();
        clientIpField.setEnabled(!checkBox.isSelected());
        if (checkBox.isSelected()) {
            clientIpField.setText(Utils.getMachineAddress());
        }
    }

    public void toggleRandomPortField(ActionEvent event) {
        final JCheckBox checkBox = (JCheckBox) event.getSource();
        final JAppTextField clientPortField = connectionWindow.getClientPortTextField();
        clientPortField.setEnabled(!checkBox.isSelected());
        if (checkBox.isSelected()) {
            clientPortField.setText(String.valueOf(Utils.getRandomPortOrDefault(443)));
        }
    }
}
