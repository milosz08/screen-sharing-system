/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.dialog;

import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;
import pl.polsl.screensharing.lib.gui.fragment.JAppAboutPanel;

import javax.swing.*;

public class AboutDialogWindow extends AbstractPopupDialog {
    private final JAppAboutPanel appAboutPanel;

    public AboutDialogWindow(HostWindow hostWindow) {
        super(AppType.HOST, 500, 200, "About", hostWindow, AboutDialogWindow.class);
        this.appAboutPanel = new JAppAboutPanel(AppType.HOST);
        initDialogGui(true);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
        rootPanel.add(appAboutPanel);
    }
}
