package pl.polsl.screensharing.host.view.dialog;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import pl.polsl.screensharing.host.controller.SessionInfoDialogController;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.SharedConstants;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;
import pl.polsl.screensharing.lib.gui.component.JAppInfoBlock;

import javax.swing.*;
import java.awt.*;

@Getter
public class SessionInfoDialogWindow extends AbstractPopupDialog {
    private final HostState hostState;
    private String password;

    private final JPanel infoDataPanel;

    private final JAppInfoBlock ipAddressInfoBlock;
    private final JAppInfoBlock portInfoBlock;
    private final JAppInfoBlock passwordInfoBlock;
    private final JAppInfoBlock hasPasswordInfoBlock;

    private final JPanel passwordPanel;
    private final JCheckBox passwordCheckbox;

    private final SessionInfoDialogController controller;

    public SessionInfoDialogWindow(HostWindow hostWindow) {
        super(AppType.HOST, 320, 180, "Session informations", hostWindow, SessionInfoDialogWindow.class);
        hostState = hostWindow.getHostState();
        controller = new SessionInfoDialogController(this);
        password = StringUtils.EMPTY;

        infoDataPanel = new JPanel(new GridLayout(4, 2));

        ipAddressInfoBlock = new JAppInfoBlock("Ip address");
        portInfoBlock = new JAppInfoBlock("Port");
        passwordInfoBlock = new JAppInfoBlock("Password", SharedConstants.PASSWORD_REPLACEMENT);
        hasPasswordInfoBlock = new JAppInfoBlock("Has password");

        passwordPanel = new JPanel();
        passwordCheckbox = new JCheckBox("Show password");

        initObservables();

        passwordCheckbox.addActionListener(controller::togglePasswordVisibility);

        initDialogGui(true);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.add(passwordInfoBlock.getValue());
        passwordPanel.add(passwordCheckbox);

        ipAddressInfoBlock.addToComponent(infoDataPanel);
        portInfoBlock.addToComponent(infoDataPanel);
        infoDataPanel.add(passwordInfoBlock.getLabel());
        infoDataPanel.add(passwordPanel);
        hasPasswordInfoBlock.addToComponent(infoDataPanel);

        rootPanel.add(infoDataPanel, BorderLayout.CENTER);
    }

    private void initObservables() {
        hostState.wrapAsDisposable(hostState.getSessionDetails$(), sessionDetails -> {
            ipAddressInfoBlock.setText(sessionDetails.getIpAddress());
            portInfoBlock.setText(sessionDetails.getPortAsStr());
            password = sessionDetails.getPassword();
            hasPasswordInfoBlock.setText(String.valueOf(sessionDetails.getHasPassword()));
            passwordCheckbox.setEnabled(sessionDetails.getHasPassword());
            passwordInfoBlock.setText(sessionDetails.getHasPassword()
                ? SharedConstants.PASSWORD_REPLACEMENT : "no password");
            passwordInfoBlock.setFontToValue(!sessionDetails.getHasPassword());
        });
    }
}
