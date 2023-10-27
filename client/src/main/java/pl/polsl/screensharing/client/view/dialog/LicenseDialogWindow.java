/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.dialog;

import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;
import pl.polsl.screensharing.lib.gui.fragment.JAppLicensePanel;

import javax.swing.*;

public class LicenseDialogWindow extends AbstractPopupDialog {
    private final JAppLicensePanel licensePanel;

    public LicenseDialogWindow(ClientWindow clientWindow) {
        super(AppType.CLIENT, 550, 400, "License", clientWindow, LicenseDialogWindow.class);
        this.licensePanel = new JAppLicensePanel();
        initDialogGui(false);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
        rootPanel.add(licensePanel.getScrollPane());
    }
}
