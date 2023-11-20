/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.tabbed;

import lombok.Getter;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.fragment.VideoCanvas;
import pl.polsl.screensharing.lib.gui.AbstractTabbedPanel;

import javax.swing.*;
import java.awt.*;

@Getter
public class TabbedVideoStreamPanel extends AbstractTabbedPanel {
    private final JPanel videoStreamHolder;
    private final VideoCanvas videoCanvas;

    public TabbedVideoStreamPanel(ClientWindow clientWindow) {
        videoStreamHolder = new JPanel();
        videoCanvas = new VideoCanvas(clientWindow, this);

        videoStreamHolder.add(videoCanvas);

        add(videoStreamHolder, BorderLayout.CENTER);
    }
}
