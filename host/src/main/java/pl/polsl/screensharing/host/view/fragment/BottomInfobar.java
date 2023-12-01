/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.fragment;

import lombok.Getter;
import pl.polsl.screensharing.host.controller.BottomInfobarController;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.Utils;
import pl.polsl.screensharing.lib.gui.AbstractBottomInfobar;
import pl.polsl.screensharing.lib.gui.fragment.JAppActionRectInfo;

import javax.swing.*;
import java.awt.*;

@Getter
public class BottomInfobar extends AbstractBottomInfobar {
    private final HostState hostState;
    private final BottomInfobarController bottomInfobarController;

    private final JLabel sessionStatusTextLabel;
    private final JLabel sessionStatusLabel;
    private final JLabel sessionTimeLabel;
    private final JAppActionRectInfo streamingRectInfo;
    private final JLabel streamingTimeLabel;
    private final JLabel fpsInfoLabel;
    private final JLabel sentBytesPerSecLabel;

    public BottomInfobar(HostWindow hostWindow) {
        hostState = hostWindow.getHostState();
        bottomInfobarController = new BottomInfobarController(hostWindow, this);

        sessionStatusTextLabel = new JLabel("Session state:");
        sessionStatusLabel = new JLabel();
        streamingRectInfo = new JAppActionRectInfo(hostState.getStreamingState$(), hostState);
        sessionTimeLabel = new JLabel();
        streamingTimeLabel = new JLabel();
        fpsInfoLabel = new JLabel();
        sentBytesPerSecLabel = new JLabel();

        initObservables();

        sessionStatusTextLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
        streamingTimeLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 20));
        fpsInfoLabel.setBorder(marginRight);

        sessionStatusLabel.setForeground(Color.GRAY);

        stateCompoundPanel.add(sessionStatusTextLabel);
        stateCompoundPanel.add(sessionStatusLabel);

        leftCompoundPanel.add(stateCompoundPanel);
        leftCompoundPanel.add(sessionTimeLabel);

        rightCompoundPanel.add(streamingRectInfo);
        rightCompoundPanel.add(streamingTimeLabel);
        rightCompoundPanel.add(memoryUsageLabel);
        rightCompoundPanel.add(fpsInfoLabel);
        rightCompoundPanel.add(sentBytesPerSecLabel);

        addPanels();
    }

    private void initObservables() {
        hostState.wrapAsDisposable(hostState.getSessionState$(), state -> {
            sessionStatusLabel.setForeground(state.getColor());
            sessionStatusLabel.setText(state.getState());
        });
        hostState.wrapAsDisposable(hostState.getSessionTime$(), time -> {
            sessionTimeLabel.setText(Utils.parseTime(time, "Session"));
        });
        hostState.wrapAsDisposable(hostState.getStreamingTime$(), time -> {
            streamingTimeLabel.setText(Utils.parseTime(time, "Streaming"));
        });
        hostState.wrapAsDisposable(hostState.getRealFpsBuffer$(), realFps -> {
            fpsInfoLabel.setText(String.format("FPS: %s", realFps));
        });
        hostState.wrapAsDisposable(hostState.getSentBytesPerSec$(), bytes -> {
            sentBytesPerSecLabel.setText(Utils.parseBytesPerSecToMegaBytes(bytes, "Sent"));
        });
    }
}
