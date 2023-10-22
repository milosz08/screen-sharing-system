/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.dialog;

import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;

import javax.swing.*;

public class LicenseDialogWindow extends AbstractPopupDialog {
    public LicenseDialogWindow(HostWindow hostWindow) {
        super(AppType.CLIENT, 450, 400, "License", hostWindow, LicenseDialogWindow.class);
        initDialogGui(false);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
    }
}
