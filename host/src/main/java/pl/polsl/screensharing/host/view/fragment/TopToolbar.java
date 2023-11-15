/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.fragment;

import lombok.Getter;
import pl.polsl.screensharing.host.controller.TopToolbarController;
import pl.polsl.screensharing.host.view.HostIcon;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.gui.component.JAppIconButton;

import javax.swing.*;
import java.awt.*;

@Getter
public class TopToolbar extends JToolBar {
    private final JAppIconButton sessionParamsButton;
    private final JAppIconButton createSessionButton;
    private final JAppIconButton removeSessionButton;

    private final JAppIconButton startVideoStreamingButton;
    private final JAppIconButton stopVideoStreamingButton;

    private final JAppIconButton showFramelessCaptureButton;
    private final JAppIconButton hideFramelessCaptureButton;

    private final TopToolbarController controller;
    private final JToolBar.Separator separator;

    public TopToolbar(HostWindow hostWindow) {
        this.controller = new TopToolbarController(hostWindow);

        this.sessionParamsButton = new JAppIconButton("Session settings", HostIcon.SERVER_SETTINGS, true);
        this.createSessionButton = new JAppIconButton("Create session", HostIcon.ADD_LINK, true);
        this.removeSessionButton = new JAppIconButton("Remove session", HostIcon.REMOVE_LINK, true, false);

        this.startVideoStreamingButton = new JAppIconButton("Start streaming", HostIcon.DEBUG_INTERACTIVE_WINDOW, true, false);
        this.stopVideoStreamingButton = new JAppIconButton("Stop streaming", HostIcon.APPLICATION_ERROR, true, false);

        this.showFramelessCaptureButton = new JAppIconButton("Show capture frame", HostIcon.VISIBLE, true, false);
        this.hideFramelessCaptureButton = new JAppIconButton("Hide capture frame", HostIcon.CLOAK_OR_HIDE, true, false);

        this.separator = new JToolBar.Separator();
        separator.setBackground(Color.GRAY);

        this.sessionParamsButton.addActionListener(e -> controller.openSessionParamsWindow());
        this.createSessionButton.addActionListener(e -> controller.createSession());
        this.removeSessionButton.addActionListener(e -> controller.removeSession());

        this.startVideoStreamingButton.addActionListener(e -> controller.startVideoStreaming());
        this.stopVideoStreamingButton.addActionListener(e -> controller.stopVideoStreaming());

        this.showFramelessCaptureButton.addActionListener(e -> controller.toggleFramelessCaptureFrame(true));
        this.hideFramelessCaptureButton.addActionListener(e -> controller.toggleFramelessCaptureFrame(false));

        addButtonWithSeparation(sessionParamsButton);
        addSeparator();
        addButtonWithSeparation(createSessionButton);
        addButtonWithSeparation(removeSessionButton);
        addSeparator();
        addButtonWithSeparation(startVideoStreamingButton);
        addButtonWithSeparation(stopVideoStreamingButton);
        addSeparator();
        addButtonWithSeparation(showFramelessCaptureButton);
        addButtonWithSeparation(hideFramelessCaptureButton);

        setFloatable(false);
    }

    private void addButtonWithSeparation(JAppIconButton button) {
        add(button);
        add(Box.createHorizontalStrut(5));
    }
}
