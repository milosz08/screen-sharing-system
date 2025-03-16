package pl.polsl.screensharing.host.controller;

import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.dialog.AboutDialogWindow;
import pl.polsl.screensharing.host.view.dialog.LicenseDialogWindow;

public class TopMenuBarController extends AbstractMenuActionController {
    public TopMenuBarController(HostWindow hostWindow) {
        super(hostWindow);
    }

    public void openAboutProgramSection() {
        final AboutDialogWindow window = hostWindow.getAboutDialogWindow();
        window.setVisible(true);
    }

    public void openLicenseSection() {
        final LicenseDialogWindow window = hostWindow.getLicenseDialogWindow();
        window.setVisible(true);
    }
}
