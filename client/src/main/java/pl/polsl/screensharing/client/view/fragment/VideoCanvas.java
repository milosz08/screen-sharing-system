/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.fragment;

import lombok.Getter;
import pl.polsl.screensharing.client.controller.VideoCanvasController;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.state.VisibilityState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.tabbed.TabbedVideoStreamPanel;
import pl.polsl.screensharing.lib.gui.lambda.ResizeComponentAdapter;

import javax.swing.*;
import java.awt.*;

@Getter
public class VideoCanvas extends JPanel {
    private final ClientWindow clientWindow;
    private final ClientState clientState;
    private final VideoCanvasController controller;

    public VideoCanvas(ClientWindow clientWindow, TabbedVideoStreamPanel tabbedVideoStreamPanel) {
        this.clientWindow = clientWindow;
        clientState = clientWindow.getClientState();

        controller = new VideoCanvasController(this, tabbedVideoStreamPanel);

        setBackground(Color.BLACK);
        initObservables();
        setLayout(new BorderLayout());
        setVisible(false);

        clientWindow.addComponentListener(new ResizeComponentAdapter(() -> {
            controller.onResizeWithAspectRatio(16.0 / 9.0);
        }));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        controller.drawContent(g);
    }

    private void initObservables() {
        clientState.wrapAsDisposable(clientState.getVisibilityState$(), visibilityState -> {
            setVisible(visibilityState.equals(VisibilityState.VISIBLE));
        });
    }
}
