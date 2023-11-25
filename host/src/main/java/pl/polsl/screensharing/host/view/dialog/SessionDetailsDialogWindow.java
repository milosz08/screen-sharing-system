/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.dialog;

import lombok.Getter;
import pl.polsl.screensharing.host.controller.SessionDetailsController;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostIcon;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.SharedConstants;
import pl.polsl.screensharing.lib.Utils;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;
import pl.polsl.screensharing.lib.gui.GridBagDrawer;
import pl.polsl.screensharing.lib.gui.component.JAppIconButton;
import pl.polsl.screensharing.lib.gui.component.JAppPasswordTextField;
import pl.polsl.screensharing.lib.gui.component.JAppTextField;
import pl.polsl.screensharing.lib.gui.icon.LibIcon;
import pl.polsl.screensharing.lib.gui.input.SimpleDocumentListener;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Base64;

@Getter
public class SessionDetailsDialogWindow extends AbstractPopupDialog {
    private final HostState hostState;

    private final JPanel formPanel;
    private final JPanel rightPanel;
    private final JPanel rightTopButtonsPanel;

    private final JPanel sessionInfoPanel;
    private final TitledBorder sessionInfoTitledBorder;
    private final GridBagConstraints sessionInfoGridBag;

    private final EmptyBorder margin;
    private final Insets gridInset;

    private final JPanel ipAddressPanel;
    private final JLabel ipAddressLabel;
    private final JAppTextField ipAddressTextField;
    private final JCheckBox isMachineIpAddress;

    private final JLabel portLabel;
    private final JAppTextField portTextField;

    private final JPanel passwordPanel;
    private final JLabel passwordLabel;
    private final JAppPasswordTextField passwordTextField;
    private final JCheckBox passwordTogglerCheckbox;
    private final JCheckBox hasPasswordCheckbox;

    private final JAppIconButton connectButton;
    private final JAppIconButton cancelButton;
    private final JAppIconButton saveDetailsButton;

    private final SessionDetailsController controller;
    private final SimpleDocumentListener documentListener;

    public SessionDetailsDialogWindow(HostWindow hostWindow) {
        super(AppType.HOST, 480, 210, "Session details", hostWindow, SessionDetailsDialogWindow.class);
        hostState = hostWindow.getHostState();

        controller = new SessionDetailsController(hostWindow, this);
        documentListener = new SimpleDocumentListener(controller::resetSaveButtonState);

        formPanel = new JPanel();
        rightPanel = new JPanel(new BorderLayout());
        rightTopButtonsPanel = new JPanel(new GridLayout(2, 1, 0, 5));

        sessionInfoPanel = new JPanel(new GridBagLayout());
        sessionInfoTitledBorder = new TitledBorder("Session details");
        sessionInfoGridBag = new GridBagConstraints();

        margin = new EmptyBorder(10, 10, 10, 10);
        gridInset = new Insets(3, 3, 3, 3);

        ipAddressPanel = new JPanel();
        ipAddressLabel = new JLabel("IP address");
        ipAddressTextField = new JAppTextField(10, 15, SharedConstants.IPV4_REGEX);
        isMachineIpAddress = new JCheckBox("Get machine IP");

        portLabel = new JLabel("Connection port");
        portTextField = new JAppTextField(10, 6, SharedConstants.PORT_REGEX);

        passwordPanel = new JPanel();
        passwordLabel = new JLabel("Password (optional)");
        passwordTextField = new JAppPasswordTextField(10);
        passwordTogglerCheckbox = new JCheckBox("Show password");
        hasPasswordCheckbox = new JCheckBox("Has Password");

        connectButton = new JAppIconButton("Create", HostIcon.ADD_LINK);
        cancelButton = new JAppIconButton("Cancel", LibIcon.CANCEL);
        saveDetailsButton = new JAppIconButton("Save", LibIcon.SAVE);

        initObservables();

        ipAddressTextField.getDocument().addDocumentListener(documentListener);
        portTextField.getDocument().addDocumentListener(documentListener);
        passwordTextField.getDocument().addDocumentListener(documentListener);

        passwordTogglerCheckbox.addActionListener(controller::togglePasswordField);
        isMachineIpAddress.addActionListener(controller::toggleMachineIpAddressField);
        hasPasswordCheckbox.addActionListener(controller::togglePasswordInputField);

        connectButton.addActionListener(e -> controller.createSession());
        cancelButton.addActionListener(e -> controller.closeWindow());
        saveDetailsButton.addActionListener(controller::saveConnectionDetails);

        initDialogGui(true);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
        sessionInfoPanel.setBorder(new CompoundBorder(sessionInfoTitledBorder, margin));

        ipAddressPanel.setLayout(new BoxLayout(ipAddressPanel, BoxLayout.Y_AXIS));
        ipAddressPanel.add(ipAddressTextField);
        ipAddressPanel.add(isMachineIpAddress);

        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.add(passwordTextField);
        passwordPanel.add(passwordTogglerCheckbox);
        passwordPanel.add(hasPasswordCheckbox);

        final GridBagDrawer basicInfoDrawer = new GridBagDrawer(sessionInfoPanel, sessionInfoGridBag, gridInset);
        basicInfoDrawer.drawGridBagLabels(ipAddressLabel, portLabel, passwordLabel);
        basicInfoDrawer.drawGridBagInputs(ipAddressPanel, portTextField, passwordPanel);

        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.add(sessionInfoPanel);

        rightTopButtonsPanel.add(connectButton);
        rightTopButtonsPanel.add(saveDetailsButton);

        rightPanel.add(rightTopButtonsPanel, BorderLayout.NORTH);
        rightPanel.add(cancelButton, BorderLayout.SOUTH);

        rootPanel.add(formPanel, BorderLayout.CENTER);
        rootPanel.add(rightPanel, BorderLayout.EAST);
    }

    private void initObservables() {
        hostState.wrapAsDisposable(hostState.getSessionDetails$(), sessionDetails -> {
            ipAddressTextField.setText(sessionDetails.isMachineIp()
                ? Utils.getMachineAddress() : sessionDetails.getIpAddress());
            ipAddressTextField.setEnabled(!sessionDetails.isMachineIp());
            isMachineIpAddress.setSelected(sessionDetails.isMachineIp());
            portTextField.setText(sessionDetails.getPortAsStr());
            passwordTextField.setText(new String(Base64.getDecoder().decode(sessionDetails.getPassword())));
            passwordTextField.setEnabled(sessionDetails.isHasPassword());
            passwordTogglerCheckbox.setEnabled(sessionDetails.isHasPassword());
            hasPasswordCheckbox.setSelected(sessionDetails.isHasPassword());
        });
    }
}
