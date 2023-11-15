/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view;

import lombok.Getter;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.dialog.AboutDialogWindow;
import pl.polsl.screensharing.host.view.dialog.ConnectionSettingsWindow;
import pl.polsl.screensharing.host.view.dialog.LicenseDialogWindow;
import pl.polsl.screensharing.host.view.fragment.TopMenuBar;
import pl.polsl.screensharing.host.view.fragment.TopToolbar;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractRootFrame;

import javax.swing.*;
import java.awt.*;

@Getter
public class HostWindow extends AbstractRootFrame {
    private final HostState hostState;

    private final AboutDialogWindow aboutDialogWindow;
    private final LicenseDialogWindow licenseDialogWindow;
    private final TopMenuBar topMenuBar;
    private final TopToolbar topToolbar;

    private final ConnectionSettingsWindow connectionSettingsWindow;
    private final CaptureFramelessWindow captureFramelessWindow;

    public HostWindow(HostState hostState) {
        super(AppType.HOST, HostWindow.class);
        this.hostState = hostState;

        this.topMenuBar = new TopMenuBar(this);
        this.topToolbar = new TopToolbar(this);
        this.captureFramelessWindow = new CaptureFramelessWindow(this);

        this.aboutDialogWindow = new AboutDialogWindow(this);
        this.licenseDialogWindow = new LicenseDialogWindow(this);
        this.connectionSettingsWindow = new ConnectionSettingsWindow(this);
    }

    @Override
    protected void extendsFrame(JFrame frame, JPanel rootPanel) {
        frame.setJMenuBar(topMenuBar);
        frame.add(topToolbar, BorderLayout.NORTH);
    }
}
