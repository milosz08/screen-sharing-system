package pl.polsl.screensharing.client.view.dialog;

import lombok.Getter;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;
import pl.polsl.screensharing.lib.gui.component.JAppInfoBlock;

import javax.swing.*;
import java.awt.*;

@Getter
public class SessionInfoDialogWindow extends AbstractPopupDialog {
    private final ClientState clientState;

    private final JPanel infoDataPanel;

    private final JAppInfoBlock hostIpAddressInfoBlock;
    private final JAppInfoBlock hostPortInfoBlock;
    private final JAppInfoBlock clientIpAddressInfoBlock;
    private final JAppInfoBlock clientPortInfoBlock;
    private final JAppInfoBlock username;

    public SessionInfoDialogWindow(ClientWindow clientWindow) {
        super(AppType.CLIENT, 320, 180, "Session informations", clientWindow, SessionInfoDialogWindow.class);
        clientState = clientWindow.getClientState();

        infoDataPanel = new JPanel(new GridLayout(5, 2));

        hostIpAddressInfoBlock = new JAppInfoBlock("Host Ip address");
        hostPortInfoBlock = new JAppInfoBlock("Host port");
        clientIpAddressInfoBlock = new JAppInfoBlock("Client Ip address");
        clientPortInfoBlock = new JAppInfoBlock("Client port");
        username = new JAppInfoBlock("Username");

        initObservables();

        initDialogGui(true);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
        hostIpAddressInfoBlock.addToComponent(infoDataPanel);
        hostPortInfoBlock.addToComponent(infoDataPanel);
        clientIpAddressInfoBlock.addToComponent(infoDataPanel);
        clientPortInfoBlock.addToComponent(infoDataPanel);
        username.addToComponent(infoDataPanel);

        rootPanel.add(infoDataPanel, BorderLayout.CENTER);
    }

    private void initObservables() {
        clientState.wrapAsDisposable(clientState.getFastConnectionDetails$(), fastConnectionDetails -> {
            hostIpAddressInfoBlock.setText(fastConnectionDetails.getHostIpAddress());
            hostPortInfoBlock.setText(fastConnectionDetails.getHostPortAsStr());
            clientIpAddressInfoBlock.setText(fastConnectionDetails.getClientIpAddress());
            clientPortInfoBlock.setText(fastConnectionDetails.getClientPortAsStr());
            username.setText(fastConnectionDetails.getUsername());
        });
    }
}
