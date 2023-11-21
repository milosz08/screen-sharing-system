/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.dialog;

import lombok.Getter;
import pl.polsl.screensharing.client.controller.ConnectController;
import pl.polsl.screensharing.client.state.ClientState;
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
    private final ClientState clientState;

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

    public ConnectWindow(ClientWindow clientWindow) {
        super(AppType.HOST, 480, 210, "Connect", clientWindow, ConnectWindow.class);
        clientState = clientWindow.getClientState();

        controller = new ConnectController(clientWindow, this);
        documentListener = new SimpleDocumentListener(controller::resetSaveButtonState);

        formPanel = new JPanel();
        rightPanel = new JPanel(new GridLayout(8, 1, 5, 5));
        addtlDataPanel = new JPanel(new GridBagLayout());

        basicInfoPanel = new JPanel(new GridBagLayout());
        basicInfoTitledBorder = new TitledBorder("Connection parameters");
        basicInfoGridBag = new GridBagConstraints();

        additlnInfoPanel = new JPanel(new GridBagLayout());
        additionalInfoTitledBorder = new TitledBorder("Additional parameters");
        addtlnInfoGridBag = new GridBagConstraints();

        margin = new EmptyBorder(10, 10, 10, 10);
        gridInset = new Insets(3, 3, 3, 3);

        ipAddressLabel = new JLabel("IP address");
        ipAddressTextField = new JAppTextField(10, 15, SharedConstants.IPV4_REGEX);

        portLabel = new JLabel("Connection port (optional)");
        portTextField = new JAppTextField(10, 6, SharedConstants.PORT_REGEX);

        usernameLabel = new JLabel("Username (optional)");
        usernameTextField = new JAppTextField(10, 50, SharedConstants.USERNAME_REGEX);

        passwordPanel = new JPanel();
        passwordLabel = new JLabel("Password");
        passwordTextField = new JAppPasswordTextField(10);
        passwordTogglerCheckbox = new JCheckBox("Show password");

        connectButton = new JAppIconButton("Connect", ClientIcon.CONNECT_TO_REMOTE_SERVER);
        cancelButton = new JAppIconButton("Cancel", LibIcon.CANCEL);
        saveDetailsButton = new JAppIconButton("Save", LibIcon.SAVE);
        addToListCheckbox = new JCheckBox("Add to list", true);

        descriptionLabel = new JLabel("Connection description (optional)");
        descriptionTextArea = new JAppTextArea(3, 30, 100);
        descriptionScrollPane = new JScrollPane(descriptionTextArea);

        initObservables();

        ipAddressTextField.getDocument().addDocumentListener(documentListener);
        portTextField.getDocument().addDocumentListener(documentListener);
        usernameTextField.getDocument().addDocumentListener(documentListener);
        descriptionTextArea.getDocument().addDocumentListener(documentListener);

        passwordTogglerCheckbox.addActionListener(controller::togglePasswordField);

        connectButton.addActionListener(e -> controller.createConnection());
        cancelButton.addActionListener(e -> controller.closeWindow());
        saveDetailsButton.addActionListener(controller::saveConnectionDetails);

        initDialogGui(true);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
        basicInfoPanel.setBorder(new CompoundBorder(basicInfoTitledBorder, margin));
        additlnInfoPanel.setBorder(new CompoundBorder(additionalInfoTitledBorder, margin));

        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
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

        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
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

    private void initObservables() {
        clientState.wrapAsDisposable(clientState.getFastConnectionDetails$(), fastConnectionsDetails -> {
            ipAddressTextField.setText(fastConnectionsDetails.getIpAddress());
            portTextField.setText(fastConnectionsDetails.getPortAsStr());
            usernameTextField.setText(fastConnectionsDetails.getUsername());
            descriptionTextArea.setText(fastConnectionsDetails.getDescription());
        });
    }
}
