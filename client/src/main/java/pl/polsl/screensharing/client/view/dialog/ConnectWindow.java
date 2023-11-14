/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.dialog;

import lombok.Getter;
import pl.polsl.screensharing.client.controller.ConnectController;
import pl.polsl.screensharing.client.dto.FastConnDetailsDto;
import pl.polsl.screensharing.client.view.ClientIcon;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.SharedConstants;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;
import pl.polsl.screensharing.lib.gui.GridBagDrawer;
import pl.polsl.screensharing.lib.gui.component.JAppIconButton;
import pl.polsl.screensharing.lib.gui.component.JAppPasswordTextField;
import pl.polsl.screensharing.lib.gui.component.JAppTextArea;
import pl.polsl.screensharing.lib.gui.component.JAppTextField;
import pl.polsl.screensharing.lib.gui.icon.LibIcon;
import pl.polsl.screensharing.lib.gui.input.SimpleDocumentListener;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

@Getter
public class ConnectWindow extends AbstractPopupDialog {
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

    private final JAppIconButton connectButton;
    private final JAppIconButton cancelButton;
    private final JAppIconButton saveDetailsButton;
    private final JCheckBox addToListCheckbox;

    private final ConnectController controller;
    private final SimpleDocumentListener documentListener;
    private final FastConnDetailsDto fastConnDetails;

    public ConnectWindow(ClientWindow clientWindow) {
        super(AppType.HOST, 480, 210, "Connect", clientWindow, ConnectWindow.class);

        this.controller = new ConnectController(clientWindow, this);
        this.documentListener = new SimpleDocumentListener(controller::resetSaveButtonState);
        this.fastConnDetails = clientWindow.getClientState().getFastConnDetails();

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
        this.ipAddressTextField = new JAppTextField(fastConnDetails.getIpAddress(), 10, 15, SharedConstants.IPV4_REGEX);

        this.portLabel = new JLabel("Connection port (optional)");
        this.portTextField = new JAppTextField(fastConnDetails.getPortAsStr(), 10, 6, SharedConstants.PORT_REGEX);

        this.usernameLabel = new JLabel("Username (optional)");
        this.usernameTextField = new JAppTextField(fastConnDetails.getUsername(), 10, 50, SharedConstants.USERNAME_REGEX);

        this.passwordPanel = new JPanel();
        this.passwordLabel = new JLabel("Password");
        this.passwordTextField = new JAppPasswordTextField(10);
        this.passwordTogglerCheckbox = new JCheckBox("Show password");

        this.connectButton = new JAppIconButton("Connect", ClientIcon.CONNECT_TO_REMOTE_SERVER);
        this.cancelButton = new JAppIconButton("Cancel", LibIcon.CANCEL);
        this.saveDetailsButton = new JAppIconButton("Save", LibIcon.SAVE);
        this.addToListCheckbox = new JCheckBox("Add to list", true);

        this.descriptionLabel = new JLabel("Connection description (optional)");
        this.descriptionTextArea = new JAppTextArea(fastConnDetails.getDescription(), 3, 30, 100);
        this.descriptionScrollPane = new JScrollPane(descriptionTextArea);

        this.ipAddressTextField.getDocument().addDocumentListener(this.documentListener);
        this.portTextField.getDocument().addDocumentListener(this.documentListener);
        this.usernameTextField.getDocument().addDocumentListener(this.documentListener);
        this.descriptionTextArea.getDocument().addDocumentListener(this.documentListener);

        this.passwordTogglerCheckbox.addActionListener(controller::togglePasswordField);

        this.connectButton.addActionListener(e -> controller.createConnection());
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

        rightPanel.add(connectButton);
        rightPanel.add(saveDetailsButton);
        rightPanel.add(addToListCheckbox);

        for (int i = 0; i < 4; i++) {
            rightPanel.add(new JPanel());
        }

        rightPanel.add(cancelButton);

        rootPanel.add(formPanel, BorderLayout.CENTER);
        rootPanel.add(rightPanel, BorderLayout.EAST);
    }
}
