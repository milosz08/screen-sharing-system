/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.fragment;

import lombok.Getter;
import pl.polsl.screensharing.host.controller.VideoCanvasController;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.tabbed.TabbedScreenFramePanel;
import pl.polsl.screensharing.lib.gui.lambda.ResizeComponentAdapter;

import javax.swing.*;
import java.awt.*;

@Getter
public class VideoCanvas extends JPanel {
    private final VideoCanvasController videoCanvasController;

    public VideoCanvas(HostWindow hostWindow, TabbedScreenFramePanel tabbedScreenFramePanel) {
        this.videoCanvasController = new VideoCanvasController(tabbedScreenFramePanel, hostWindow);
        hostWindow.addComponentListener(new ResizeComponentAdapter(videoCanvasController::onResizeWithAspectRatio));

        setBackground(Color.BLACK);
        setLayout(new FlowLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // to remove
        g.setColor(Color.GREEN);
        g.fillRect(50, 50, 100, 80);
    }
}
