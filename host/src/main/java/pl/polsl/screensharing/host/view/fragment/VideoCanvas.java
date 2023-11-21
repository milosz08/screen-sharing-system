/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.fragment;

import io.reactivex.rxjava3.core.Observable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.aggregator.StreamingStateFrameModeAggregator;
import pl.polsl.screensharing.host.controller.VideoCanvasController;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.StreamingState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.tabbed.TabbedScreenFramePanel;
import pl.polsl.screensharing.lib.gui.lambda.ResizeComponentAdapter;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Getter
public class VideoCanvas extends JPanel {
    private final HostState hostState;
    private final VideoCanvasController controller;
    private final FrameSelectorPanel frameSelectorPanel;

    private GraphicsDevice graphicsDevice;
    private StreamingState streamingState;
    private final AtomicBoolean isScreenShowingForParticipants;
    private final AtomicBoolean isCursorShowing;

    public VideoCanvas(HostWindow hostWindow, TabbedScreenFramePanel tabbedScreenFramePanel) {
        hostState = hostWindow.getHostState();
        controller = new VideoCanvasController(hostWindow, this, tabbedScreenFramePanel);

        frameSelectorPanel = new FrameSelectorPanel(hostWindow, this);
        isScreenShowingForParticipants = new AtomicBoolean(true);
        isCursorShowing = new AtomicBoolean(true);
        streamingState = StreamingState.STOPPED;

        setBackground(Color.BLACK);
        initObservables();
        setLayout(null);

        hostWindow.addComponentListener(new ResizeComponentAdapter(controller::onResizeWithAspectRatio));

        add(frameSelectorPanel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        controller.renderContent(g);
    }

    private void initObservables() {
        final Observable<StreamingStateFrameModeAggregator> aggregator = Observable.combineLatest(
            hostState.getStreamingState$(),
            hostState.isFrameSelectorShowing$(),
            StreamingStateFrameModeAggregator::new);

        hostState.wrapAsDisposable(aggregator, state -> {
            if (!state.isFrameVisible()) {
                final StreamingState streamingState = state.getStreamingState();
                setBorder(streamingState.equals(StreamingState.STREAMING)
                    ? BorderFactory.createLineBorder(streamingState.getColor(), 3) : null);
            }
        });
        hostState.wrapAsDisposable(hostState.getSelectedGraphicsDevice$(), graphicsDevice -> {
            this.graphicsDevice = graphicsDevice;
            if (!controller.isAlive()) {
                controller.start();
            }
            controller.onResizeWithAspectRatio();
            repaint();
        });
        hostState.wrapAsDisposable(hostState.isScreenIsShowForParticipants$(), isShowing -> {
            isScreenShowingForParticipants.set(isShowing);
            setVisible(isShowing);
        });
        hostState.wrapAsDisposable(hostState.isCursorShowing$(), isCursorShowing::set);
        hostState.wrapAsDisposable(hostState.getStreamingState$(), streamingState -> {
            this.streamingState = streamingState;
        });
    }
}
