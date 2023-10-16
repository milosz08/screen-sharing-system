/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.gui;

import pl.polsl.screensharing.client.controller.EstabilishedConnectionController;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;
import pl.polsl.screensharing.lib.gui.GridBagDrawer;
import pl.polsl.screensharing.lib.gui.components.JAppPasswordTextField;
import pl.polsl.screensharing.lib.gui.components.JAppTextField;
import pl.polsl.screensharing.lib.gui.input.SimpleDocumentListener;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class EstablishedConnectionWindow extends AbstractPopupDialog {
    private final JPanel formPanel;
    private final JPanel rightPanel;

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

    private final JButton estabilishedConnButton;
    private final JButton cancelButton;
    private final JButton saveDetailsButton;
    private final JCheckBox addToListCheckbox;

    private final EstabilishedConnectionController controller;
    private final SimpleDocumentListener documentListener;

    public EstablishedConnectionWindow(ClientWindow clientWindow) {
        super(AppType.HOST, 480, 210, "Established connection", clientWindow, EstablishedConnectionWindow.class);

        this.controller = new EstabilishedConnectionController(this);
        this.documentListener = new SimpleDocumentListener(controller::resetSaveButtonState);

        this.formPanel = new JPanel();
        this.rightPanel = new JPanel(new GridLayout(6, 1, 5, 5));

        this.basicInfoPanel = new JPanel(new GridBagLayout());
        this.basicInfoTitledBorder = new TitledBorder("Connection parameters");
        this.basicInfoGridBag = new GridBagConstraints();

        this.additlnInfoPanel = new JPanel(new GridBagLayout());
        this.additionalInfoTitledBorder = new TitledBorder("Additional parameters");
        this.addtlnInfoGridBag = new GridBagConstraints();

        this.margin = new EmptyBorder(10, 10, 10, 10);
        this.gridInset = new Insets(3, 3, 3, 3);

        this.ipAddressLabel = new JLabel("IP address");
        this.ipAddressTextField = new JAppTextField("127.0.0.1", 10, 15, "^[0-9.]+$");

        this.portLabel = new JLabel("Connection port (optional)");
        this.portTextField = new JAppTextField("9191", 10, 6, "^[0-9]+$");

        this.usernameLabel = new JLabel("Username (optional)");
        this.usernameTextField = new JAppTextField(10, 50, "^[0-9a-z]+$");

        this.passwordPanel = new JPanel();
        this.passwordLabel = new JLabel("Password");
        this.passwordTextField = new JAppPasswordTextField(10);
        this.passwordTogglerCheckbox = new JCheckBox("Show password");

        this.estabilishedConnButton = new JButton("Connect >>");
        this.cancelButton = new JButton("Cancel");
        this.saveDetailsButton = new JButton("Save");
        this.addToListCheckbox = new JCheckBox("Add to list", true);

        this.ipAddressTextField.getDocument().addDocumentListener(this.documentListener);
        this.portTextField.getDocument().addDocumentListener(this.documentListener);
        this.usernameTextField.getDocument().addDocumentListener(this.documentListener);

        this.passwordTogglerCheckbox.addActionListener(controller::togglePasswordField);

        this.estabilishedConnButton.addActionListener(e -> controller.estabilishedConnection());
        this.cancelButton.addActionListener(e -> controller.closeWindow());
        this.saveDetailsButton.addActionListener(controller::saveConnectionDetails);

        initDialogGui();
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

        final GridBagDrawer addtlnInfoDrawer = new GridBagDrawer(additlnInfoPanel, addtlnInfoGridBag, gridInset);
        addtlnInfoDrawer.drawGridBagLabels(usernameLabel, passwordLabel);
        addtlnInfoDrawer.drawGridBagInputs(usernameTextField, passwordPanel);

        formPanel.setLayout(new BoxLayout(this.formPanel, BoxLayout.Y_AXIS));
        formPanel.add(basicInfoPanel);
        formPanel.add(additlnInfoPanel);

        rightPanel.add(estabilishedConnButton);
        rightPanel.add(saveDetailsButton);
        rightPanel.add(addToListCheckbox);
        rightPanel.add(new JPanel());
        rightPanel.add(new JPanel());
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

    public JButton getSaveDetailsButton() {
        return saveDetailsButton;
    }
}
