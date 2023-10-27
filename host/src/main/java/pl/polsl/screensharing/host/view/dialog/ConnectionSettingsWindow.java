/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.dialog;

import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;

import javax.swing.*;

public class ConnectionSettingsWindow extends AbstractPopupDialog {
    public ConnectionSettingsWindow(HostWindow hostWindow) {
        super(AppType.HOST, 480, 210, "Connection settings", hostWindow, ConnectionSettingsWindow.class);

        initDialogGui(true);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
    }
}
