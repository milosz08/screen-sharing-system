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
import pl.polsl.screensharing.lib.icon.LibIcon;

import javax.swing.*;

@Getter
public class TopMenuBar extends JMenuBar {
    private final TopMenuBarController controller;
    private final HostState hostState;

    private final JMenu sessionMenu;
    private final JMenu videoStreamMenu;
    private final JMenu screenMenu;
    private final JMenu helpMenu;

    private final JAppMenuIconItem createSessionMenuItem;
    private final JAppMenuIconItem removeSessionMenuItem;
    private final JAppMenuIconItem sessionInfoMenuItem;

    private final JAppMenuIconItem startVideoStreamingMenuItem;
    private final JAppMenuIconItem stopVideoStreamingMenuItem;

    private final JAppMenuIconItem showScreenToParticipantsMenuItem;
    private final JAppMenuIconItem hideScreenFromParticipantsMenuItem;

    private final JAppMenuIconItem aboutMenuItem;
    private final JAppMenuIconItem licenseMenuItem;

    private final JAppMenuIconItem[] sessionMenuItems;
    private final JAppMenuIconItem[] videoStreamMenuItems;
    private final JAppMenuIconItem[] screenMenuItems;
    private final JAppMenuIconItem[] helpMenuItems;

    public TopMenuBar(HostWindow hostWindow) {
        controller = new TopMenuBarController(hostWindow);
        hostState = hostWindow.getHostState();

        sessionMenu = new JMenu("Session");
        videoStreamMenu = new JMenu("Video stream");
        screenMenu = new JMenu("Screen");
        helpMenu = new JMenu("Help");

        createSessionMenuItem = new JAppMenuIconItem("Create session", HostIcon.ADD_LINK);
        removeSessionMenuItem = new JAppMenuIconItem("Remove session", HostIcon.REMOVE_LINK, false);
        sessionInfoMenuItem = new JAppMenuIconItem("Session info", LibIcon.STATUS_INFORMATION, false);

        startVideoStreamingMenuItem = new JAppMenuIconItem("Start streaming", HostIcon.DEBUG_INTERACTIVE_WINDOW, false);
        stopVideoStreamingMenuItem = new JAppMenuIconItem("Stop streaming", HostIcon.APPLICATION_ERROR, false);

        showScreenToParticipantsMenuItem = new JAppMenuIconItem("Show screen", HostIcon.OPEN_QUERY, false);
        hideScreenFromParticipantsMenuItem = new JAppMenuIconItem("Hide screen", HostIcon.STOP_QUERY, false);

        aboutMenuItem = new JAppMenuIconItem("About", LibIcon.HELP_TABLE_OF_CONTENTS);
        licenseMenuItem = new JAppMenuIconItem("License", LibIcon.CODE_INFORMATION_RULE);

        initObservables();

        sessionMenuItems = new JAppMenuIconItem[]{
            createSessionMenuItem,
            removeSessionMenuItem,
            sessionInfoMenuItem,
        };
        videoStreamMenuItems = new JAppMenuIconItem[]{
            startVideoStreamingMenuItem,
            stopVideoStreamingMenuItem,
        };
        screenMenuItems = new JAppMenuIconItem[]{
            showScreenToParticipantsMenuItem,
            hideScreenFromParticipantsMenuItem,
        };
        helpMenuItems = new JAppMenuIconItem[]{
            aboutMenuItem,
            licenseMenuItem,
        };

        createSessionMenuItem.addActionListener(e -> controller.openSessionDetailsWindow());
        removeSessionMenuItem.addActionListener(e -> controller.removeSession());
        sessionInfoMenuItem.addActionListener(e -> controller.openSessionInfoWindow());

        startVideoStreamingMenuItem.addActionListener(e -> controller.startVideoStreaming());
        stopVideoStreamingMenuItem.addActionListener(e -> controller.stopVideoStreaming());

        showScreenToParticipantsMenuItem.addActionListener(e -> controller.toggleScreenShowingForParticipants(true));
        hideScreenFromParticipantsMenuItem.addActionListener(e -> controller.toggleScreenShowingForParticipants(false));

        aboutMenuItem.addActionListener(e -> controller.openAboutProgramSection());
        licenseMenuItem.addActionListener(e -> controller.openLicenseSection());

        addMenuItems(sessionMenu, sessionMenuItems);
        addMenuItems(videoStreamMenu, videoStreamMenuItems);
        addMenuItems(screenMenu, screenMenuItems);
        addMenuItems(helpMenu, helpMenuItems);

        add(sessionMenu);
        add(videoStreamMenu);
        add(screenMenu);
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
            createSessionMenuItem.setEnabled(!isCreated);
            removeSessionMenuItem.setEnabled(isCreated);
            sessionInfoMenuItem.setEnabled(isCreated);
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
        hostState.wrapAsDisposable(hostState.isScreenIsShowForParticipants$(), isShowing -> {
            showScreenToParticipantsMenuItem.setEnabled(!isShowing);
            hideScreenFromParticipantsMenuItem.setEnabled(isShowing);
        });
    }
}
