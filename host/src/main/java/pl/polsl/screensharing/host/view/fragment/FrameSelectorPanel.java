/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.fragment;

import io.reactivex.rxjava3.core.Observable;
import pl.polsl.screensharing.host.aggregator.StreamingStateFrameModeAggregator;
import pl.polsl.screensharing.host.gfx.MoveAndDraggableListener;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.StreamingState;
import pl.polsl.screensharing.host.view.HostWindow;

import javax.swing.*;
import java.awt.*;

public class FrameSelectorPanel extends JPanel {
    private final HostState hostState;
    private final Dimension size;
    private final MoveAndDraggableListener moveAndDraggableListener;

    private Color frameColor;

    public FrameSelectorPanel(HostWindow hostWindow, VideoCanvas videoCanvas) {
        hostState = hostWindow.getHostState();
        size = new Dimension(400, 250);
        moveAndDraggableListener = new MoveAndDraggableListener(this, videoCanvas, size);

        setBounds(0, 0, size.width, size.height);
        setLayout(null);
        setOpaque(false);

        initObservables();

        addMouseListener(moveAndDraggableListener);
        addMouseMotionListener(moveAndDraggableListener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (frameColor != null) {
            g.setColor(frameColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        super.paintComponent(g);
    }

    private void initObservables() {
        final Observable<StreamingStateFrameModeAggregator> aggregator = Observable.combineLatest(
            hostState.getStreamingState$(),
            hostState.isFrameSelectorShowing$(),
            StreamingStateFrameModeAggregator::new);

        hostState.wrapAsDisposable(aggregator, state -> {
            if (state.isFrameVisible()) {
                final StreamingState streamingState = state.getStreamingState();
                setBorder(streamingState.equals(StreamingState.STREAMING)
                    ? BorderFactory.createLineBorder(streamingState.getColor(), 3) : null);
            }
        });
        hostState.wrapAsDisposable(hostState.isFrameSelectorShowing$(), this::setVisible);
        hostState.wrapAsDisposable(hostState.getFrameColor$(), color -> {
            frameColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 80);
        });
    }
}
