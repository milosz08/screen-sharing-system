/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.gui;

import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;

import javax.swing.*;

public class LastConnectionsWindow extends AbstractPopupDialog {
    public LastConnectionsWindow(ClientWindow clientWindow) {
        super(AppType.HOST, 400, 300, "Last connections", clientWindow, LastConnectionsWindow.class);
        initDialogGui();
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
    }
}
