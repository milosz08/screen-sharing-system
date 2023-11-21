/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.dialog;

import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;
import pl.polsl.screensharing.lib.gui.fragment.JAppAboutPanel;

import javax.swing.*;

public class AboutDialogWindow extends AbstractPopupDialog {
    private final JAppAboutPanel appAboutPanel;

    public AboutDialogWindow(ClientWindow clientWindow) {
        super(AppType.CLIENT, 500, 200, "About", clientWindow, AboutDialogWindow.class);
        appAboutPanel = new JAppAboutPanel(AppType.CLIENT);
        initDialogGui(true);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
        rootPanel.add(appAboutPanel);
    }
}
