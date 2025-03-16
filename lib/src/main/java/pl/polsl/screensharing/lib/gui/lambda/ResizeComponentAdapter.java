package pl.polsl.screensharing.lib.gui.lambda;

import lombok.RequiredArgsConstructor;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

@RequiredArgsConstructor
public class ResizeComponentAdapter extends ComponentAdapter {
    private final Runnable runnable;

    @Override
    public void componentResized(ComponentEvent e) {
        runnable.run();
    }
}
