/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.dialog;

import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;

import javax.swing.*;

public class ParticipantsDialogWindow extends AbstractPopupDialog {
    public ParticipantsDialogWindow(HostWindow hostWindow) {
        super(AppType.HOST, 430, 300, "Participants", hostWindow, ParticipantsDialogWindow.class);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
    }
}
