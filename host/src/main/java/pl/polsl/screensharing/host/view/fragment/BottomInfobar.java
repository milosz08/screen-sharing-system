/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.fragment;

import lombok.Getter;
import pl.polsl.screensharing.host.controller.BottomInfobarController;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.Parser;
import pl.polsl.screensharing.lib.gui.AbstractBottomInfobar;
import pl.polsl.screensharing.lib.gui.fragment.JAppIntegrityActionRectInfo;

import javax.swing.*;
import java.awt.*;

@Getter
public class BottomInfobar extends AbstractBottomInfobar {
    private final HostState hostState;
    private final BottomInfobarController bottomInfobarController;

    private final JLabel sessionStatusTextLabel;
    private final JLabel sessionStatusLabel;
    private final JLabel sessionTimeLabel;
    private final JAppIntegrityActionRectInfo streamingRectInfo;
    private final JLabel streamingTimeLabel;
    private final JLabel fpsInfoLabel;
    private final JLabel sendBytesPerSecLabel;

    public BottomInfobar(HostWindow hostWindow) {
        this.hostState = hostWindow.getHostState();
        this.bottomInfobarController = new BottomInfobarController(hostWindow, this);

        this.sessionStatusTextLabel = new JLabel("Session state:");
        this.sessionStatusLabel = new JLabel(hostState.getSessionState().getState());
        this.streamingRectInfo = new JAppIntegrityActionRectInfo(hostState);
        this.sessionTimeLabel = new JLabel(Parser.parseTime(hostState.getSessionTime(), "Session"));
        this.streamingTimeLabel = new JLabel(Parser.parseTime(hostState.getStreamingTime(), "Streaming"));
        this.fpsInfoLabel = new JLabel(parseFpsState(hostState.getStreamingFps()));
        this.sendBytesPerSecLabel = new JLabel(Parser.parseBytesPerSecState(hostState.getSendBytesPerSec(), "Send"));

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
        rightCompoundPanel.add(sendBytesPerSecLabel);

        addPanels();
    }

    public String parseFpsState(int fps) {
        return String.format("FPS: %s", fps);
    }
}
