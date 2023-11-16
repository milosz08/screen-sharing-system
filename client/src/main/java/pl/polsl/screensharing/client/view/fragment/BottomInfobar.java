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
import pl.polsl.screensharing.lib.gui.fragment.JAppActionRectInfo;

import javax.swing.*;
import java.awt.*;

@Getter
public class BottomInfobar extends AbstractBottomInfobar {
    private final ClientState clientState;
    private final BottomInfobarController bottomInfobarController;

    private final JLabel connectionStatusTextLabel;
    private final JLabel connectionStatusLabel;
    private final JLabel connectionTimeLabel;
    private final JAppActionRectInfo recordingRectInfo;
    private final JLabel recordingTimeLabel;
    private final JLabel recvBytesPerSecLabel;

    public BottomInfobar(ClientWindow clientWindow) {
        this.clientState = clientWindow.getClientState();
        this.bottomInfobarController = new BottomInfobarController(clientWindow, this);

        this.connectionStatusTextLabel = new JLabel("Connection state:");
        this.connectionStatusLabel = new JLabel();
        this.connectionTimeLabel = new JLabel();
        this.recordingRectInfo = new JAppActionRectInfo(clientState.getRecordingState$(), clientState);
        this.recordingTimeLabel = new JLabel();
        this.recvBytesPerSecLabel = new JLabel();

        initObservables();

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

    private void initObservables() {
        clientState.wrapAsDisposable(clientState.getConnectionState$(), state -> {
            connectionStatusLabel.setForeground(state.getColor());
            connectionStatusLabel.setText(state.getState());
        });
        clientState.wrapAsDisposable(clientState.getConnectionTime$(), time -> {
            connectionTimeLabel.setText(Parser.parseTime(time, "Connection"));
        });
        clientState.wrapAsDisposable(clientState.getRecordingTime$(), time -> {
            recordingTimeLabel.setText(Parser.parseTime(time, "Recording"));
        });
        clientState.wrapAsDisposable(clientState.getRecvBytesPerSec$(), bytes -> {
            recvBytesPerSecLabel.setText(Parser.parseBytesPerSecState(bytes, "Received"));
        });
    }
}
