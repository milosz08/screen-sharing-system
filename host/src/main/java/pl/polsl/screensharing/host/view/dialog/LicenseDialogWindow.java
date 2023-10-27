/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.dialog;

import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;
import pl.polsl.screensharing.lib.gui.fragment.JAppLicensePanel;

import javax.swing.*;

public class LicenseDialogWindow extends AbstractPopupDialog {
    private final JAppLicensePanel licensePanel;

    public LicenseDialogWindow(HostWindow hostWindow) {
        super(AppType.HOST, 550, 400, "License", hostWindow, LicenseDialogWindow.class);
        this.licensePanel = new JAppLicensePanel();
        initDialogGui(false);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
        rootPanel.add(licensePanel.getScrollPane());
    }
}
