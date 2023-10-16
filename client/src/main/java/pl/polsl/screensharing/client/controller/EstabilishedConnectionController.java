/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import pl.polsl.screensharing.client.gui.EstablishedConnectionWindow;
import pl.polsl.screensharing.lib.gui.components.JAppPasswordTextField;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class EstabilishedConnectionController {
    private final EstablishedConnectionWindow connectionWindow;

    private boolean isSaveEnabled;

    public EstabilishedConnectionController(EstablishedConnectionWindow connectionWindow) {
        this.connectionWindow = connectionWindow;
    }

    public void estabilishedConnection() {
        final String host = connectionWindow.getIpAddressTextField().getText();
        final String port = connectionWindow.getPortTextField().getText();
        final String username = connectionWindow.getUsernameTextField().getText();
        final String password = new String(connectionWindow.getPasswordTextField().getPassword());
        final boolean isSaving = connectionWindow.getAddToListCheckbox().isSelected();

        // TODO: check ip address, check port

        System.out.println("dialing up... bip boop bip");
        System.out.printf("%s:%s, username: %s, password: %s, is saving: %s%n", host, port, username, password, isSaving);

        closeWindow();
    }

    public void closeWindow() {
        connectionWindow.setVisible(false);
        connectionWindow.dispose();
        isSaveEnabled = true;
    }

    public void saveConnectionDetails(ActionEvent event) {
        final JButton saveDetailsButton = (JButton) event.getSource();
        saveDetailsButton.setEnabled(false);
        isSaveEnabled = false;
        JOptionPane.showConfirmDialog(connectionWindow, "Your connection details was successfully saved.",
            "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
    }

    public void resetSaveButtonState() {
        final JButton saveDetailsButton = connectionWindow.getSaveDetailsButton();
        saveDetailsButton.setEnabled(!isSaveEnabled);

    }

    public void togglePasswordField(ActionEvent event) {
        final JAppPasswordTextField passwordField = connectionWindow.getPasswordTextField();
        final JCheckBox checkBox = (JCheckBox) event.getSource();
        passwordField.toggleVisibility(checkBox.isSelected());
    }
}