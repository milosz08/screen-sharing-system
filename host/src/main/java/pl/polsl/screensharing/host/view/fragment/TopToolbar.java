/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.fragment;

import io.reactivex.rxjava3.core.Observable;
import lombok.Getter;
import pl.polsl.screensharing.host.aggregator.SessionStreamingAggregator;
import pl.polsl.screensharing.host.controller.TopToolbarController;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.SessionState;
import pl.polsl.screensharing.host.state.StreamingState;
import pl.polsl.screensharing.host.view.HostIcon;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.gui.component.JAppIconButton;
import pl.polsl.screensharing.lib.icon.LibIcon;

import javax.swing.*;

@Getter
public class TopToolbar extends JToolBar {
    private final HostState hostState;

    private final JAppIconButton createSessionButton;
    private final JAppIconButton removeSessionButton;
    private final JAppIconButton sessionInfoButton;

    private final JAppIconButton startVideoStreamingButton;
    private final JAppIconButton stopVideoStreamingButton;

    private final JAppIconButton showScreenToParticipantsButton;
    private final JAppIconButton hideScreenFromParticipantsButton;

    private final TopToolbarController controller;

    public TopToolbar(HostWindow hostWindow) {
        controller = new TopToolbarController(hostWindow);
        hostState = hostWindow.getHostState();

        createSessionButton = new JAppIconButton("Create session", HostIcon.ADD_LINK, true);
        removeSessionButton = new JAppIconButton("Remove session", HostIcon.REMOVE_LINK, true, false);
        sessionInfoButton = new JAppIconButton("Session info", LibIcon.STATUS_INFORMATION, true, false);

        startVideoStreamingButton = new JAppIconButton("Start streaming", HostIcon.DEBUG_INTERACTIVE_WINDOW, true, false);
        stopVideoStreamingButton = new JAppIconButton("Stop streaming", HostIcon.APPLICATION_ERROR, true, false);

        showScreenToParticipantsButton = new JAppIconButton("Show screen", HostIcon.OPEN_QUERY, true, false);
        hideScreenFromParticipantsButton = new JAppIconButton("Hide screen", HostIcon.STOP_QUERY, true);

        initObservables();

        createSessionButton.addActionListener(e -> controller.openSessionDetailsWindow());
        removeSessionButton.addActionListener(e -> controller.removeSession());
        sessionInfoButton.addActionListener(e -> controller.openSessionInfoWindow());

        startVideoStreamingButton.addActionListener(e -> controller.startVideoStreaming());
        stopVideoStreamingButton.addActionListener(e -> controller.stopVideoStreaming());

        showScreenToParticipantsButton.addActionListener(e -> controller.toggleScreenShowingForParticipants(true));
        hideScreenFromParticipantsButton.addActionListener(e -> controller.toggleScreenShowingForParticipants(false));

        addButtonWithSeparation(createSessionButton);
        addButtonWithSeparation(removeSessionButton);
        addButtonWithSeparation(sessionInfoButton);
        addSeparator();
        addButtonWithSeparation(startVideoStreamingButton);
        addButtonWithSeparation(stopVideoStreamingButton);
        addSeparator();
        addButtonWithSeparation(showScreenToParticipantsButton);
        addButtonWithSeparation(hideScreenFromParticipantsButton);

        setFloatable(false);
    }

    private void addButtonWithSeparation(JAppIconButton button) {
        add(button);
        add(Box.createHorizontalStrut(5));
    }

    private void initObservables() {
        hostState.wrapAsDisposable(hostState.getSessionState$(), state -> {
            final boolean isCreated = state.equals(SessionState.CREATED);
            createSessionButton.setEnabled(!isCreated);
            removeSessionButton.setEnabled(isCreated);
            sessionInfoButton.setEnabled(isCreated);
        });

        final Observable<SessionStreamingAggregator> aggregator = Observable.combineLatest(
            hostState.getSessionState$(),
            hostState.getStreamingState$(),
            SessionStreamingAggregator::new);

        hostState.wrapAsDisposable(aggregator, state -> {
            final boolean isCreated = state.getSessionState().equals(SessionState.CREATED);
            final boolean isStreaming = state.getStreamingState().equals(StreamingState.STREAMING);
            startVideoStreamingButton.setEnabled(!isStreaming && isCreated);
            stopVideoStreamingButton.setEnabled(isStreaming && isCreated);
        });
        hostState.wrapAsDisposable(hostState.isScreenIsShowForParticipants$(), isShowing -> {
            showScreenToParticipantsButton.setEnabled(!isShowing);
            hideScreenFromParticipantsButton.setEnabled(isShowing);
        });
    }
}
