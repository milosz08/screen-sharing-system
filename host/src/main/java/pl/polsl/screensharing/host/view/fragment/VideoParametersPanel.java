/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.fragment;

import io.reactivex.rxjava3.core.Observable;
import lombok.Getter;
import pl.polsl.screensharing.host.aggregator.SessionStreamingAggregator;
import pl.polsl.screensharing.host.controller.VideoParametersController;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.SessionState;
import pl.polsl.screensharing.host.state.StreamingState;
import pl.polsl.screensharing.host.view.HostIcon;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.gui.component.JAppIconButton;

import javax.swing.*;
import java.awt.*;

@Getter
public class VideoParametersPanel extends JPanel {
    private final HostState hostState;

    private final JPanel rootSettingsPanel;
    private final CaptureSettingsPanel captureSettingsPanel;
    private final GraphicsSettingsPanel graphicsSettingsPanel;

    private final JPanel actionButtonsPanel;
    private final GridBagConstraints gridBagConstraints;
    private final JAppIconButton showParticipantsButton;
    private final JAppIconButton startVideoStreamingButton;
    private final JAppIconButton stopVideoStreamingButton;
    private final JAppIconButton showScreenToParticipantsButton;
    private final JAppIconButton hideScreenToParticipantsButton;

    private final VideoParametersController controller;

    public VideoParametersPanel(HostWindow hostWindow) {
        hostState = hostWindow.getHostState();
        controller = new VideoParametersController(hostWindow);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        captureSettingsPanel = new CaptureSettingsPanel(hostWindow);
        graphicsSettingsPanel = new GraphicsSettingsPanel(hostWindow);

        rootSettingsPanel = new JPanel(new BorderLayout());
        rootSettingsPanel.add(captureSettingsPanel, BorderLayout.NORTH);
        rootSettingsPanel.add(graphicsSettingsPanel, BorderLayout.SOUTH);

        actionButtonsPanel = new JPanel(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(3, 5, 3, 5);

        startVideoStreamingButton = new JAppIconButton("Start stream", HostIcon.DEBUG_INTERACTIVE_WINDOW, false);
        stopVideoStreamingButton = new JAppIconButton("Stop stream", HostIcon.APPLICATION_ERROR, false, false);
        showScreenToParticipantsButton = new JAppIconButton("Show screen", HostIcon.OPEN_QUERY, false, false);
        hideScreenToParticipantsButton = new JAppIconButton("Hide screen", HostIcon.STOP_QUERY, false);
        showParticipantsButton = new JAppIconButton("Show participants (0)", HostIcon.LOOKUP_GROUP_MEMBERS, false);

        initObservables();

        startVideoStreamingButton.addActionListener(e -> controller.startVideoStreaming());
        stopVideoStreamingButton.addActionListener(e -> controller.stopVideoStreaming());
        showScreenToParticipantsButton.addActionListener(e -> controller.toggleScreenShowingForParticipants(true));
        hideScreenToParticipantsButton.addActionListener(e -> controller.toggleScreenShowingForParticipants(false));
        showParticipantsButton.addActionListener(e -> controller.showParticipantsDialog());

        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        addToGridBag(startVideoStreamingButton, 0, 0);
        addToGridBag(showScreenToParticipantsButton, 1, gridBagConstraints.gridy);
        addToGridBag(stopVideoStreamingButton, 0, 1);
        addToGridBag(hideScreenToParticipantsButton, 1, 1);
        addToGridBag(showParticipantsButton, 0, 2, 2);

        add(rootSettingsPanel, BorderLayout.NORTH);
        add(new JPanel(), BorderLayout.CENTER);
        add(actionButtonsPanel, BorderLayout.SOUTH);
    }

    private void addToGridBag(JComponent component, int x, int y, int width) {
        gridBagConstraints.gridx = x;
        gridBagConstraints.gridy = y;
        gridBagConstraints.gridwidth = width;
        actionButtonsPanel.add(component, gridBagConstraints);
    }

    private void addToGridBag(JComponent component, int x, int y) {
        addToGridBag(component, x, y, 1);
    }

    private void initObservables() {
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
            hideScreenToParticipantsButton.setEnabled(isShowing);
        });
        hostState.wrapAsDisposable(hostState.getConnectedClientsInfo$(), clients -> {
            showParticipantsButton.setText(String.format("Show participants (%s)", clients.size()));
        });
    }
}
