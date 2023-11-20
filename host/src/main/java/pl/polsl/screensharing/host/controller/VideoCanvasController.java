/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import lombok.RequiredArgsConstructor;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.tabbed.TabbedScreenFramePanel;

import javax.swing.*;
import java.awt.*;

@RequiredArgsConstructor
public class VideoCanvasController {
    private final TabbedScreenFramePanel tabbedScreenFramePanel;
    private final HostWindow hostWindow;

    public void onResizeWithAspectRatio() {
        final JPanel videoFrameHolder = tabbedScreenFramePanel.getVideoFrameHolder();
        final Rectangle screenSize = videoCanvas.getGraphicsDevice().getDefaultConfiguration().getBounds();
        final Dimension size = Utils
            .calcSizeBaseAspectRatio(videoFrameHolder, screenSize.getWidth() / screenSize.getHeight());
        tabbedScreenFramePanel.getVideoCanvas().setPreferredSize(size);
        tabbedScreenFramePanel.getDisableScreenPanel().setPreferredSize(size);
        videoFrameHolder.revalidate();
    }
}
