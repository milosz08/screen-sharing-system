/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.dialog.AboutDialogWindow;
import pl.polsl.screensharing.client.view.dialog.LicenseDialogWindow;

public class TopMenuBarController extends AbstractMenuActionController {
    public TopMenuBarController(ClientWindow clientWindow) {
        super(clientWindow);
    }

    public void openAboutProgramSection() {
        final AboutDialogWindow window = clientWindow.getAboutDialogWindow();
        window.setVisible(true);
    }

    public void openLicenseSection() {
        final LicenseDialogWindow window = clientWindow.getLicenseDialogWindow();
        window.setVisible(true);
    }
}
