/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.fragment;

import io.reactivex.rxjava3.core.Observable;
import lombok.Getter;
import pl.polsl.screensharing.host.aggregator.SessionStreamingAggregator;
import pl.polsl.screensharing.host.controller.TopMenuBarController;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.SessionState;
import pl.polsl.screensharing.host.state.StreamingState;
import pl.polsl.screensharing.host.view.HostIcon;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.gui.component.JAppMenuIconItem;
import pl.polsl.screensharing.lib.gui.icon.LibIcon;

import javax.swing.*;

@Getter
public class TopMenuBar extends JMenuBar {
    private final TopMenuBarController controller;
    private final HostState hostState;

    private final JMenu sessionMenu;
    private final JMenu videoStreamMenu;
    private final JMenu helpMenu;

    private final JAppMenuIconItem sessionParamsMenuItem;
    private final JAppMenuIconItem createSessionMenuItem;
    private final JAppMenuIconItem removeSessionMenuItem;

    private final JAppMenuIconItem startVideoStreamingMenuItem;
    private final JAppMenuIconItem stopVideoStreamingMenuItem;

    private final JAppMenuIconItem aboutMenuItem;
    private final JAppMenuIconItem licenseMenuItem;

    private final JAppMenuIconItem[] sessionMenuItems;
    private final JAppMenuIconItem[] videoStreamMenuItems;
    private final JAppMenuIconItem[] helpMenuItems;

    public TopMenuBar(HostWindow hostWindow) {
        this.controller = new TopMenuBarController(hostWindow);
        this.hostState = hostWindow.getHostState();

        this.sessionMenu = new JMenu("Session");
        this.videoStreamMenu = new JMenu("Video stream");
        this.helpMenu = new JMenu("Help");

        this.sessionParamsMenuItem = new JAppMenuIconItem("Session settings", HostIcon.SERVER_SETTINGS);
        this.createSessionMenuItem = new JAppMenuIconItem("Create session", HostIcon.ADD_LINK);
        this.removeSessionMenuItem = new JAppMenuIconItem("Remove session", HostIcon.REMOVE_LINK, false);

        this.startVideoStreamingMenuItem = new JAppMenuIconItem("Start streaming", HostIcon.DEBUG_INTERACTIVE_WINDOW, false);
        this.stopVideoStreamingMenuItem = new JAppMenuIconItem("Stop streaming", HostIcon.APPLICATION_ERROR, false);

        this.aboutMenuItem = new JAppMenuIconItem("About", LibIcon.HELP_TABLE_OF_CONTENTS);
        this.licenseMenuItem = new JAppMenuIconItem("License", LibIcon.CODE_INFORMATION_RULE);

        initObservables();

        this.sessionMenuItems = new JAppMenuIconItem[]{
            sessionParamsMenuItem,
            createSessionMenuItem,
            removeSessionMenuItem,
        };
        this.videoStreamMenuItems = new JAppMenuIconItem[]{
            startVideoStreamingMenuItem,
            stopVideoStreamingMenuItem,
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

        this.aboutMenuItem.addActionListener(e -> controller.openAboutProgramSection());
        this.licenseMenuItem.addActionListener(e -> controller.openLicenseSection());

        addMenuItems(sessionMenu, sessionMenuItems);
        addMenuItems(videoStreamMenu, videoStreamMenuItems);
        addMenuItems(helpMenu, helpMenuItems);

        add(sessionMenu);
        add(videoStreamMenu);
        add(helpMenu);
    }

    private void addMenuItems(JMenu menu, JMenuItem[] items) {
        for (final JMenuItem item : items) {
            menu.add(item);
        }
    }

    private void initObservables() {
        hostState.wrapAsDisposable(hostState.getSessionState$(), state -> {
            final boolean isCreated = state.equals(SessionState.CREATED);
            sessionParamsMenuItem.setEnabled(!isCreated);
            createSessionMenuItem.setEnabled(!isCreated);
            removeSessionMenuItem.setEnabled(isCreated);
        });

        final Observable<SessionStreamingAggregator> aggregator = Observable.combineLatest(
            hostState.getSessionState$(),
            hostState.getStreamingState$(),
            SessionStreamingAggregator::new);

        hostState.wrapAsDisposable(aggregator, aggregated -> {
            final boolean isCreated = aggregated.getSessionState().equals(SessionState.CREATED);
            final boolean isStreaming = aggregated.getStreamingState().equals(StreamingState.STREAMING);
            startVideoStreamingMenuItem.setEnabled(!isStreaming && isCreated);
            stopVideoStreamingMenuItem.setEnabled(isStreaming && isCreated);
        });
    }
}
