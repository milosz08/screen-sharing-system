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
        int containerWidth = videoFrameHolder.getWidth();
        int containerHeight = videoFrameHolder.getHeight();

        final double aspectRatio = 16.0 / 9.0;
        int width;
        int height;

        if ((double) containerWidth / containerHeight > aspectRatio) {
            height = containerHeight;
            width = (int) (height * aspectRatio);
        } else {
            width = containerWidth;
            height = (int) (width / aspectRatio);
        }
        tabbedScreenFramePanel.getVideoCanvas().setPreferredSize(new Dimension(width, height));
        videoFrameHolder.revalidate();
    }
}
