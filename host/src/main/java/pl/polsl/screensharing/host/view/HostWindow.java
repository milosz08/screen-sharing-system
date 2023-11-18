/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view;

import lombok.Getter;
import pl.polsl.screensharing.host.controller.BottomInfobarController;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.dialog.AboutDialogWindow;
import pl.polsl.screensharing.host.view.dialog.ConnectionSettingsDialogWindow;
import pl.polsl.screensharing.host.view.dialog.LicenseDialogWindow;
import pl.polsl.screensharing.host.view.dialog.ParticipantsDialogWindow;
import pl.polsl.screensharing.host.view.fragment.BottomInfobar;
import pl.polsl.screensharing.host.view.fragment.TopMenuBar;
import pl.polsl.screensharing.host.view.fragment.TopToolbar;
import pl.polsl.screensharing.host.view.tabbed.TabbedPaneWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractRootFrame;

import javax.swing.*;
import java.awt.*;

@Getter
public class HostWindow extends AbstractRootFrame {
    private final HostState hostState;

    private final TopMenuBar topMenuBar;
    private final TopToolbar topToolbar;
    private final TabbedPaneWindow tabbedPaneWindow;
    private final BottomInfobar bottomInfobar;

    private final AboutDialogWindow aboutDialogWindow;
    private final LicenseDialogWindow licenseDialogWindow;
    private final ConnectionSettingsDialogWindow connectionSettingsDialogWindow;
    private final ParticipantsDialogWindow participantsDialogWindow;

    public HostWindow(HostState hostState) {
        super(AppType.HOST, hostState, HostWindow.class);
        this.hostState = hostState;

        this.topMenuBar = new TopMenuBar(this);
        this.topToolbar = new TopToolbar(this);
        this.tabbedPaneWindow = new TabbedPaneWindow(this);
        this.bottomInfobar = new BottomInfobar(this);

        this.aboutDialogWindow = new AboutDialogWindow(this);
        this.licenseDialogWindow = new LicenseDialogWindow(this);
        this.connectionSettingsDialogWindow = new ConnectionSettingsDialogWindow(this);
        this.participantsDialogWindow = new ParticipantsDialogWindow(this);
    }

    @Override
    protected void extendsFrame(JFrame frame, JPanel rootPanel) {
        frame.setJMenuBar(topMenuBar);
        frame.add(topToolbar, BorderLayout.NORTH);
        frame.add(tabbedPaneWindow, BorderLayout.CENTER);
        frame.add(bottomInfobar, BorderLayout.SOUTH);
    }

    public BottomInfobarController getBottomInfobarController() {
        return bottomInfobar.getBottomInfobarController();
    }
}
