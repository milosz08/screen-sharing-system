/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.fragment;

import lombok.Getter;
import pl.polsl.screensharing.client.controller.BottomInfobarController;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.Parser;
import pl.polsl.screensharing.lib.gui.AbstractBottomInfobar;
import pl.polsl.screensharing.lib.gui.fragment.JAppIntegrityActionRectInfo;

import javax.swing.*;
import java.awt.*;

@Getter
public class BottomInfobar extends AbstractBottomInfobar {
    private final ClientState clientState;
    private final BottomInfobarController bottomInfobarController;

    private final JLabel connectionStatusTextLabel;
    private final JLabel connectionStatusLabel;
    private final JLabel connectionTimeLabel;
    private final JAppIntegrityActionRectInfo recordingRectInfo;
    private final JLabel recordingTimeLabel;
    private final JLabel recvBytesPerSecLabel;

    public BottomInfobar(ClientWindow clientWindow) {
        this.clientState = clientWindow.getClientState();
        this.bottomInfobarController = new BottomInfobarController(clientWindow, this);

        this.connectionStatusTextLabel = new JLabel("Connection state:");
        this.connectionStatusLabel = new JLabel(clientState.getConnectionState().getState());
        this.connectionTimeLabel = new JLabel(Parser.parseTime(clientState.getConnectionTime(), "Connection"));
        this.recordingRectInfo = new JAppIntegrityActionRectInfo(clientState);
        this.recordingTimeLabel = new JLabel(Parser.parseTime(clientState.getRecordingTime(), "Recording"));
        this.recvBytesPerSecLabel = new JLabel(Parser.parseBytesPerSecState(clientState.getRecvBytesPerSec(), "Received"));

        connectionStatusTextLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
        connectionStatusLabel.setBorder(marginRight);
        recordingTimeLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 20));

        connectionStatusLabel.setForeground(Color.GRAY);

        stateCompoundPanel.add(connectionStatusTextLabel);
        stateCompoundPanel.add(connectionStatusLabel);

        leftCompoundPanel.add(stateCompoundPanel);
        leftCompoundPanel.add(connectionTimeLabel);

        rightCompoundPanel.add(recordingRectInfo);
        rightCompoundPanel.add(recordingTimeLabel);
        rightCompoundPanel.add(memoryUsageLabel);
        rightCompoundPanel.add(recvBytesPerSecLabel);

        addPanels();
    }
}
