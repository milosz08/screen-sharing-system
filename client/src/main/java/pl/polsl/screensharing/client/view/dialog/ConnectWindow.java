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
import pl.polsl.screensharing.lib.gui.input.SimpleDocumentListener;
import pl.polsl.screensharing.lib.icon.LibIcon;

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
    private final JPanel rightTopPanel;
    private final JPanel addtlDataPanel;

    private final JPanel hostDetailsPanel;
    private final TitledBorder hostDetailsTitledBorder;
    private final GridBagConstraints hostDetailsGridBag;

    private final JPanel clientDetailsPanel;
    private final TitledBorder clientDetailsTitledBorder;
    private final GridBagConstraints clientDetailsGridBag;

    private final JPanel additlnInfoPanel;
    private final TitledBorder additionalInfoTitledBorder;
    private final GridBagConstraints addtlnInfoGridBag;

    private final EmptyBorder margin;
    private final Insets gridInset;

    private final JLabel hostIpAddressLabel;
    private final JAppTextField hostIpAddressTextField;

    private final JLabel hostPortLabel;
    private final JAppTextField hostPortTextField;

    private final JPanel clientIpAddressPanel;
    private final JLabel clientIpAddressLabel;
    private final JAppTextField clientIpAddressTextField;
    private final JCheckBox isClientMachineIpAddressCheckbox;

    private final JPanel clientPortPanel;
    private final JLabel clientPortLabel;
    private final JAppTextField clientPortTextField;
    private final JCheckBox isClientRandomPortCheckbox;

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

        formPanel = new JPanel(new FlowLayout());
        rightPanel = new JPanel(new BorderLayout());
        rightTopPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        addtlDataPanel = new JPanel(new GridBagLayout());

        hostDetailsPanel = new JPanel(new GridBagLayout());
        hostDetailsTitledBorder = new TitledBorder("Host connection");
        hostDetailsGridBag = new GridBagConstraints();

        clientDetailsPanel = new JPanel(new GridBagLayout());
        clientDetailsTitledBorder = new TitledBorder("Client connection");
        clientDetailsGridBag = new GridBagConstraints();

        additlnInfoPanel = new JPanel(new GridBagLayout());
        additionalInfoTitledBorder = new TitledBorder("Additional parameters");
        addtlnInfoGridBag = new GridBagConstraints();

        margin = new EmptyBorder(10, 10, 10, 10);
        gridInset = new Insets(3, 3, 3, 3);

        hostIpAddressLabel = new JLabel("IP address");
        hostIpAddressTextField = new JAppTextField(10, 15, SharedConstants.IPV4_REGEX);

        hostPortLabel = new JLabel("Connection port");
        hostPortTextField = new JAppTextField(10, 6, SharedConstants.PORT_REGEX);

        clientIpAddressPanel = new JPanel();
        clientIpAddressLabel = new JLabel("IP address");
        clientIpAddressTextField = new JAppTextField(10, 15, SharedConstants.IPV4_REGEX);
        isClientMachineIpAddressCheckbox = new JCheckBox("Get machine IP");

        clientPortPanel = new JPanel();
        clientPortLabel = new JLabel("Connection port");
        clientPortTextField = new JAppTextField(10, 6, SharedConstants.PORT_REGEX);
        isClientRandomPortCheckbox = new JCheckBox("Get random port");

        usernameLabel = new JLabel("Username");
        usernameTextField = new JAppTextField(10, 50, SharedConstants.USERNAME_REGEX);

        passwordPanel = new JPanel();
        passwordLabel = new JLabel("Password (optional)");
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

        hostIpAddressTextField.getDocument().addDocumentListener(documentListener);
        hostPortTextField.getDocument().addDocumentListener(documentListener);
        usernameTextField.getDocument().addDocumentListener(documentListener);
        descriptionTextArea.getDocument().addDocumentListener(documentListener);

        passwordTogglerCheckbox.addActionListener(controller::togglePasswordField);
        isClientMachineIpAddressCheckbox.addActionListener(controller::toggleMachineIpField);
        isClientRandomPortCheckbox.addActionListener(controller::toggleRandomPortField);

        connectButton.addActionListener(e -> controller.createConnection());
        cancelButton.addActionListener(e -> controller.closeWindow());
        saveDetailsButton.addActionListener(controller::saveConnectionDetails);

        initDialogGui(true);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
        hostDetailsPanel.setBorder(new CompoundBorder(hostDetailsTitledBorder, margin));
        clientDetailsPanel.setBorder(new CompoundBorder(clientDetailsTitledBorder, margin));
        additlnInfoPanel.setBorder(new CompoundBorder(additionalInfoTitledBorder, margin));

        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.add(passwordTextField);
        passwordPanel.add(passwordTogglerCheckbox);

        clientIpAddressPanel.setLayout(new BoxLayout(clientIpAddressPanel, BoxLayout.Y_AXIS));
        clientIpAddressPanel.add(clientIpAddressTextField);
        clientIpAddressPanel.add(isClientMachineIpAddressCheckbox);

        clientPortPanel.setLayout(new BoxLayout(clientPortPanel, BoxLayout.Y_AXIS));
        clientPortPanel.add(clientPortTextField);
        clientPortPanel.add(isClientRandomPortCheckbox);

        final GridBagDrawer hostDetailsDrawer = new GridBagDrawer(hostDetailsPanel, hostDetailsGridBag, gridInset);
        hostDetailsDrawer.drawGridBagLabels(hostIpAddressLabel, hostPortLabel);
        hostDetailsDrawer.drawGridBagInputs(hostIpAddressTextField, hostPortTextField);

        final GridBagDrawer clientDetailsDrawer = new GridBagDrawer(clientDetailsPanel, clientDetailsGridBag, gridInset);
        clientDetailsDrawer.drawGridBagLabels(clientIpAddressLabel, clientPortLabel);
        clientDetailsDrawer.drawGridBagInputs(clientIpAddressPanel, clientPortPanel);

        final GridBagDrawer addtlnInfoDrawer = new GridBagDrawer(addtlDataPanel, addtlnInfoGridBag, gridInset);
        addtlnInfoDrawer.drawGridBagLabels(usernameLabel, passwordLabel);
        addtlnInfoDrawer.drawGridBagInputs(usernameTextField, passwordPanel);

        descriptionTextArea.setLineWrap(true);

        final GridBagDrawer textareaDrawer = new GridBagDrawer(additlnInfoPanel, addtlnInfoGridBag, new Insets(0, 0, 3, 0));
        textareaDrawer.drawGridBagLabels(addtlDataPanel, descriptionLabel, descriptionScrollPane);

        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.add(hostDetailsPanel);
        formPanel.add(clientDetailsPanel);
        formPanel.add(additlnInfoPanel);

        rightTopPanel.add(connectButton);
        rightTopPanel.add(saveDetailsButton);
        rightTopPanel.add(addToListCheckbox);

        rightPanel.add(rightTopPanel, BorderLayout.NORTH);
        rightPanel.add(cancelButton, BorderLayout.SOUTH);

        rootPanel.add(formPanel, BorderLayout.CENTER);
        rootPanel.add(rightPanel, BorderLayout.EAST);
    }

    private void initObservables() {
        clientState.wrapAsDisposable(clientState.getFastConnectionDetails$(), fastConnectionsDetails -> {
            hostIpAddressTextField.setText(fastConnectionsDetails.getHostIpAddress());
            hostPortTextField.setText(fastConnectionsDetails.getHostPortAsStr());
            clientIpAddressTextField.setText(fastConnectionsDetails.getClientIpAddress());
            clientIpAddressTextField.setEnabled(!fastConnectionsDetails.getIsMachineIpAddress());
            isClientMachineIpAddressCheckbox.setSelected(fastConnectionsDetails.getIsMachineIpAddress());
            clientPortTextField.setText(fastConnectionsDetails.getClientPortAsStr());
            clientPortTextField.setEnabled(!fastConnectionsDetails.getIsRandomPort());
            isClientRandomPortCheckbox.setSelected(fastConnectionsDetails.getIsRandomPort());
            usernameTextField.setText(fastConnectionsDetails.getUsername());
            descriptionTextArea.setText(fastConnectionsDetails.getDescription());
        });
    }
}
