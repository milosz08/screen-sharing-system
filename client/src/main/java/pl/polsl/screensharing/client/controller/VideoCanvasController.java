/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import pl.polsl.screensharing.client.view.fragment.VideoCanvas;
import pl.polsl.screensharing.client.view.tabbed.TabbedVideoStreamPanel;
import pl.polsl.screensharing.lib.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@Slf4j
public class VideoCanvasController {
    private final VideoCanvas videoCanvas;
    private final TabbedVideoStreamPanel tabbedVideoStreamPanel;

    @Getter
    @Setter
    private BufferedImage receivedImage;

    public VideoCanvasController(VideoCanvas videoCanvas, TabbedVideoStreamPanel tabbedVideoStreamPanel) {
        this.videoCanvas = videoCanvas;
        this.tabbedVideoStreamPanel = tabbedVideoStreamPanel;
    }

    public void onResizeWithAspectRatio(double aspectRatio) {
        final JPanel videoFrameHolder = tabbedVideoStreamPanel.getVideoStreamHolder();
        final Dimension size = Utils
            .calcSizeBaseAspectRatio(videoFrameHolder, aspectRatio);
        if (videoCanvas != null && tabbedVideoStreamPanel.getConnectionStatusPanel() != null) {
            videoCanvas.setPreferredSize(size);
            tabbedVideoStreamPanel.getConnectionStatusPanel().setPreferredSize(size);
            videoFrameHolder.revalidate();
        }
    }

    public void drawContent(Graphics graphics) {
        final Dimension size = videoCanvas.getSize();
        if (receivedImage != null) {
            graphics.drawImage(Scalr.resize(receivedImage, size.width, size.height), 0, 0, videoCanvas);
        }
    }
}
