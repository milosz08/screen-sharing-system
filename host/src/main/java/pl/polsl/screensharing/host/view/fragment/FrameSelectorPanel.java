package pl.polsl.screensharing.host.view.fragment;

import io.reactivex.rxjava3.core.Observable;
import pl.polsl.screensharing.host.aggregator.StreamingStateFrameModeAggregator;
import pl.polsl.screensharing.host.gfx.MoveAndDraggableListener;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.StreamingState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.Utils;

import javax.swing.*;
import java.awt.*;

public class FrameSelectorPanel extends JPanel {
    private static final int DEFAULT_WIDTH = 300;

    private final HostState hostState;
    private final MoveAndDraggableListener moveAndDraggableListener;

    private Color frameColor;
    private Rectangle previousGraphicsDevice;

    public FrameSelectorPanel(HostWindow hostWindow, VideoCanvas videoCanvas) {
        hostState = hostWindow.getHostState();
        moveAndDraggableListener = new MoveAndDraggableListener(this, videoCanvas);

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
    }

    private void initObservables() {
        final Observable<StreamingStateFrameModeAggregator> aggregator = Observable.combineLatest(
            hostState.getStreamingState$(),
            hostState.isFrameSelectorShowing$(),
            StreamingStateFrameModeAggregator::new);

        hostState.wrapAsDisposable(aggregator, state -> {
            final StreamingState streamingState = state.getStreamingState();
            setBorder(streamingState.equals(StreamingState.STREAMING)
                ? BorderFactory.createLineBorder(streamingState.getColor(), 2) : null);
            moveAndDraggableListener.toogleActionResizingCapability(streamingState.equals(StreamingState.STREAMING));
        });
        hostState.wrapAsDisposable(hostState.isFrameSelectorShowing$(), this::setVisible);
        hostState.wrapAsDisposable(hostState.getFrameColor$(), color -> {
            frameColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 80);
        });
        hostState.wrapAsDisposable(hostState.getSelectedGraphicsDevice$(), graphicsDevice -> {
            final Rectangle bounds = graphicsDevice.getDefaultConfiguration().getBounds();
            final Dimension size = Utils.calcHeightBaseWidthAndAR(DEFAULT_WIDTH, bounds);
            if (previousGraphicsDevice == null || Utils.checkIfSizeNotExact(bounds, previousGraphicsDevice)) {
                setSize(size);
                moveAndDraggableListener.setMinSize(size);
                previousGraphicsDevice = bounds;
            }
        });
    }
}
