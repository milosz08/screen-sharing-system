/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.tabbed;

import lombok.Getter;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.fragment.VideoCanvas;
import pl.polsl.screensharing.host.view.fragment.VideoParametersPanel;
import pl.polsl.screensharing.lib.gui.AbstractTabbedPanel;

import javax.swing.*;
import java.awt.*;

@Getter
public class TabbedScreenFramePanel extends AbstractTabbedPanel {
    private final JPanel videoFrameHolder;
    private final VideoCanvas videoCanvas;
    private final VideoParametersPanel videoParametersPanel;

    public TabbedScreenFramePanel(HostWindow hostWindow) {
        videoFrameHolder = new JPanel();

        videoCanvas = new VideoCanvas(hostWindow, this);
        videoParametersPanel = new VideoParametersPanel(hostWindow);

        videoFrameHolder.add(videoCanvas);

        add(videoFrameHolder, BorderLayout.CENTER);
        add(videoParametersPanel, BorderLayout.EAST);
    }
}