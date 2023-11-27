/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import lombok.RequiredArgsConstructor;
import pl.polsl.screensharing.host.view.dialog.SessionInfoDialogWindow;
import pl.polsl.screensharing.lib.SharedConstants;
import pl.polsl.screensharing.lib.gui.component.JAppInfoBlock;

import javax.swing.*;
import java.awt.event.ActionEvent;

@RequiredArgsConstructor
public class SessionInfoDialogController {
    private final SessionInfoDialogWindow sessionInfoDialogWindow;

    public void togglePasswordVisibility(ActionEvent event) {
        final JCheckBox checkBox = (JCheckBox) event.getSource();
        final JAppInfoBlock passwordInfoBlock = sessionInfoDialogWindow.getPasswordInfoBlock();
        String passwordContent = SharedConstants.PASSWORD_REPLACEMENT;
        if (checkBox.isSelected()) {
            passwordContent = sessionInfoDialogWindow.getPassword();
        }
        passwordInfoBlock.setText(passwordContent);
    }
}
