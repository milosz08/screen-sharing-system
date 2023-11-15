/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.fragment;

import lombok.Getter;
import pl.polsl.screensharing.host.controller.TopMenuBarController;
import pl.polsl.screensharing.host.view.HostIcon;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.gui.component.JAppMenuIconItem;
import pl.polsl.screensharing.lib.gui.icon.LibIcon;

import javax.swing.*;

@Getter
public class TopMenuBar extends JMenuBar {
    private final TopMenuBarController controller;

    private final JMenu sessionMenu;
    private final JMenu videoStreamMenu;
    private final JMenu captureFrameMenu;
    private final JMenu helpMenu;

    private final JAppMenuIconItem sessionParamsMenuItem;
    private final JAppMenuIconItem createSessionMenuItem;
    private final JAppMenuIconItem removeSessionMenuItem;

    private final JAppMenuIconItem startVideoStreamingMenuItem;
    private final JAppMenuIconItem stopVideoStreamingMenuItem;
    private final JAppMenuIconItem showFramelessCaptureMenuItem;
    private final JAppMenuIconItem hideFramelessCaptureMenuItem;

    private final JAppMenuIconItem aboutMenuItem;
    private final JAppMenuIconItem licenseMenuItem;

    private final JAppMenuIconItem[] sessionMenuItems;
    private final JAppMenuIconItem[] videoStreamMenuItems;
    private final JAppMenuIconItem[] captureFrameMenuItems;
    private final JAppMenuIconItem[] helpMenuItems;

    public TopMenuBar(HostWindow hostWindow) {
        this.controller = new TopMenuBarController(hostWindow);

        this.sessionMenu = new JMenu("Session");
        this.videoStreamMenu = new JMenu("Video stream");
        this.captureFrameMenu = new JMenu("Capture frame");
        this.helpMenu = new JMenu("Help");

        this.sessionParamsMenuItem = new JAppMenuIconItem("Session settings", HostIcon.SERVER_SETTINGS);
        this.createSessionMenuItem = new JAppMenuIconItem("Create session", HostIcon.ADD_LINK);
        this.removeSessionMenuItem = new JAppMenuIconItem("Remove session", HostIcon.REMOVE_LINK, false);

        this.startVideoStreamingMenuItem = new JAppMenuIconItem("Start streaming", HostIcon.DEBUG_INTERACTIVE_WINDOW, false);
        this.stopVideoStreamingMenuItem = new JAppMenuIconItem("Stop streaming", HostIcon.APPLICATION_ERROR, false);

        this.showFramelessCaptureMenuItem = new JAppMenuIconItem("Show capture frame", HostIcon.VISIBLE, false);
        this.hideFramelessCaptureMenuItem = new JAppMenuIconItem("Hide capture frame", HostIcon.CLOAK_OR_HIDE, false);

        this.aboutMenuItem = new JAppMenuIconItem("About", LibIcon.HELP_TABLE_OF_CONTENTS);
        this.licenseMenuItem = new JAppMenuIconItem("License", LibIcon.CODE_INFORMATION_RULE);

        this.sessionMenuItems = new JAppMenuIconItem[]{
            sessionParamsMenuItem,
            createSessionMenuItem,
            removeSessionMenuItem,
        };
        this.videoStreamMenuItems = new JAppMenuIconItem[]{
            startVideoStreamingMenuItem,
            stopVideoStreamingMenuItem,
        };
        this.captureFrameMenuItems = new JAppMenuIconItem[]{
            showFramelessCaptureMenuItem,
            hideFramelessCaptureMenuItem,
        };
        this.helpMenuItems = new JAppMenuIconItem[]{
            aboutMenuItem,
            licenseMenuItem,
        };

        this.sessionParamsMenuItem.addActionListener(e -> controller.openSessionParamsWindow());
        this.createSessionMenuItem.addActionListener(e -> controller.createSession());
        this.removeSessionMenuItem.addActionListener(e -> controller.removeSession());

        this.startVideoStreamingMenuItem.addActionListener(e -> controller.startVideoStreaming());
        this.stopVideoStreamingMenuItem.addActionListener(e -> controller.stopVideoStreaming());

        this.showFramelessCaptureMenuItem.addActionListener(e -> controller.toggleFramelessCaptureFrame(true));
        this.hideFramelessCaptureMenuItem.addActionListener(e -> controller.toggleFramelessCaptureFrame(false));

        this.aboutMenuItem.addActionListener(e -> controller.openAboutProgramSection());
        this.licenseMenuItem.addActionListener(e -> controller.openLicenseSection());

        addMenuItems(sessionMenu, sessionMenuItems);
        addMenuItems(videoStreamMenu, videoStreamMenuItems);
        addMenuItems(captureFrameMenu, captureFrameMenuItems);
        addMenuItems(helpMenu, helpMenuItems);

        add(sessionMenu);
        add(videoStreamMenu);
        add(captureFrameMenu);
        add(helpMenu);
    }

    private void addMenuItems(JMenu menu, JMenuItem[] items) {
        for (final JMenuItem item : items) {
            menu.add(item);
        }
    }
}
