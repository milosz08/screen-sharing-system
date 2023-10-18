/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.dialog;

import pl.polsl.screensharing.client.controller.EstabilishedConnectionController;
import pl.polsl.screensharing.client.dto.ConnectionDetailsDto;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.AppIcon;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.SharedConstants;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;
import pl.polsl.screensharing.lib.gui.GridBagDrawer;
import pl.polsl.screensharing.lib.gui.component.JAppIconButton;
import pl.polsl.screensharing.lib.gui.component.JAppPasswordTextField;
import pl.polsl.screensharing.lib.gui.component.JAppTextArea;
import pl.polsl.screensharing.lib.gui.component.JAppTextField;
import pl.polsl.screensharing.lib.gui.input.SimpleDocumentListener;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class EstablishedConnectionWindow extends AbstractPopupDialog {
    private final JPanel formPanel;
    private final JPanel rightPanel;
    private final JPanel addtlDataPanel;

    private final JPanel basicInfoPanel;
    private final TitledBorder basicInfoTitledBorder;
    private final GridBagConstraints basicInfoGridBag;

    private final JPanel additlnInfoPanel;
    private final TitledBorder additionalInfoTitledBorder;
    private final GridBagConstraints addtlnInfoGridBag;

    private final EmptyBorder margin;
    private final Insets gridInset;

    private final JLabel ipAddressLabel;
    private final JAppTextField ipAddressTextField;

    private final JLabel portLabel;
    private final JAppTextField portTextField;

    private final JLabel usernameLabel;
    private final JAppTextField usernameTextField;

    private final JPanel passwordPanel;
    private final JLabel passwordLabel;
    private final JAppPasswordTextField passwordTextField;
    private final JCheckBox passwordTogglerCheckbox;

    private final JScrollPane descriptionScrollPane;
    private final JLabel descriptionLabel;
    private final JAppTextArea descriptionTextArea;

    private final JAppIconButton estabilishedConnButton;
    private final JAppIconButton cancelButton;
    private final JAppIconButton saveDetailsButton;
    private final JCheckBox addToListCheckbox;

    private final EstabilishedConnectionController controller;
    private final SimpleDocumentListener documentListener;
    private final ConnectionDetailsDto connDetails;

    public EstablishedConnectionWindow(ClientWindow clientWindow) {
        super(AppType.HOST, 480, 210, "Established connection", clientWindow, EstablishedConnectionWindow.class);

        this.controller = new EstabilishedConnectionController(clientWindow, this);
        this.documentListener = new SimpleDocumentListener(controller::resetSaveButtonState);
        this.connDetails = clientWindow.getCurrentState().getConnectionDetails();

        this.formPanel = new JPanel();
        this.rightPanel = new JPanel(new GridLayout(8, 1, 5, 5));
        this.addtlDataPanel = new JPanel(new GridBagLayout());

        this.basicInfoPanel = new JPanel(new GridBagLayout());
        this.basicInfoTitledBorder = new TitledBorder("Connection parameters");
        this.basicInfoGridBag = new GridBagConstraints();

        this.additlnInfoPanel = new JPanel(new GridBagLayout());
        this.additionalInfoTitledBorder = new TitledBorder("Additional parameters");
        this.addtlnInfoGridBag = new GridBagConstraints();

        this.margin = new EmptyBorder(10, 10, 10, 10);
        this.gridInset = new Insets(3, 3, 3, 3);

        this.ipAddressLabel = new JLabel("IP address");
        this.ipAddressTextField = new JAppTextField(connDetails.getHostAddress(), 10, 15, SharedConstants.IPV4_REGEX);

        this.portLabel = new JLabel("Connection port (optional)");
        this.portTextField = new JAppTextField(String.valueOf(connDetails.getIpv4().getPort()), 10, 6, SharedConstants.PORT_REGEX);

        this.usernameLabel = new JLabel("Username (optional)");
        this.usernameTextField = new JAppTextField(connDetails.getUsername(), 10, 50, SharedConstants.USERNAME_REGEX);

        this.passwordPanel = new JPanel();
        this.passwordLabel = new JLabel("Password");
        this.passwordTextField = new JAppPasswordTextField(10);
        this.passwordTogglerCheckbox = new JCheckBox("Show password");

        this.estabilishedConnButton = new JAppIconButton("Connect", AppIcon.CONNECT_TO_REMOTE_SERVER);
        this.cancelButton = new JAppIconButton("Cancel", AppIcon.CANCEL);
        this.saveDetailsButton = new JAppIconButton("Save", AppIcon.SAVE);
        this.addToListCheckbox = new JCheckBox("Add to list", true);

        this.descriptionLabel = new JLabel("Connection description (optional)");
        this.descriptionTextArea = new JAppTextArea(connDetails.getDescription(), 3, 30, 100);
        this.descriptionScrollPane = new JScrollPane(descriptionTextArea);

        this.ipAddressTextField.getDocument().addDocumentListener(this.documentListener);
        this.portTextField.getDocument().addDocumentListener(this.documentListener);
        this.usernameTextField.getDocument().addDocumentListener(this.documentListener);
        this.descriptionTextArea.getDocument().addDocumentListener(this.documentListener);

        this.passwordTogglerCheckbox.addActionListener(controller::togglePasswordField);

        this.estabilishedConnButton.addActionListener(e -> controller.estabilishedConnection());
        this.cancelButton.addActionListener(e -> controller.closeWindow());
        this.saveDetailsButton.addActionListener(controller::saveConnectionDetails);

        initDialogGui(true);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
        basicInfoPanel.setBorder(new CompoundBorder(basicInfoTitledBorder, margin));
        additlnInfoPanel.setBorder(new CompoundBorder(additionalInfoTitledBorder, margin));

        passwordPanel.setLayout(new BoxLayout(this.passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.add(passwordTextField);
        passwordPanel.add(passwordTogglerCheckbox);

        final GridBagDrawer basicInfoDrawer = new GridBagDrawer(basicInfoPanel, basicInfoGridBag, gridInset);
        basicInfoDrawer.drawGridBagLabels(ipAddressLabel, portLabel);
        basicInfoDrawer.drawGridBagInputs(ipAddressTextField, portTextField);

        final GridBagDrawer addtlnInfoDrawer = new GridBagDrawer(addtlDataPanel, addtlnInfoGridBag, gridInset);
        addtlnInfoDrawer.drawGridBagLabels(usernameLabel, passwordLabel);
        addtlnInfoDrawer.drawGridBagInputs(usernameTextField, passwordPanel);

        descriptionTextArea.setLineWrap(true);

        final GridBagDrawer textareaDrawer = new GridBagDrawer(additlnInfoPanel, addtlnInfoGridBag, new Insets(0, 0, 3, 0));
        textareaDrawer.drawGridBagLabels(addtlDataPanel, descriptionLabel, descriptionScrollPane);

        formPanel.setLayout(new BoxLayout(this.formPanel, BoxLayout.Y_AXIS));
        formPanel.add(basicInfoPanel);
        formPanel.add(additlnInfoPanel);

        rightPanel.add(estabilishedConnButton);
        rightPanel.add(saveDetailsButton);
        rightPanel.add(addToListCheckbox);

        for (int i = 0; i < 4; i++) {
            rightPanel.add(new JPanel());
        }

        rightPanel.add(cancelButton);

        rootPanel.add(formPanel, BorderLayout.CENTER);
        rootPanel.add(rightPanel, BorderLayout.EAST);
    }

    public JAppTextField getIpAddressTextField() {
        return ipAddressTextField;
    }

    public JAppTextField getPortTextField() {
        return portTextField;
    }

    public JAppTextField getUsernameTextField() {
        return usernameTextField;
    }

    public JAppPasswordTextField getPasswordTextField() {
        return passwordTextField;
    }

    public JCheckBox getAddToListCheckbox() {
        return addToListCheckbox;
    }

    public JAppIconButton getSaveDetailsButton() {
        return saveDetailsButton;
    }

    public JAppTextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }
}
