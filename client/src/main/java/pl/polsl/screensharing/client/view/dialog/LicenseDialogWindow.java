/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.dialog;

import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;

import javax.swing.*;

public class LicenseDialogWindow extends AbstractPopupDialog {
    public LicenseDialogWindow(ClientWindow clientWindow) {
        super(AppType.CLIENT, 450, 400, "License", clientWindow, LicenseDialogWindow.class);
        initDialogGui(false);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
    }
}
