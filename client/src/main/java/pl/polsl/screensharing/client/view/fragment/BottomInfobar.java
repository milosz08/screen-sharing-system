/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.fragment;

import lombok.Getter;
import pl.polsl.screensharing.client.controller.BottomInfobarController;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.Utils;
import pl.polsl.screensharing.lib.gui.AbstractBottomInfobar;

import javax.swing.*;
import java.awt.*;

@Getter
public class BottomInfobar extends AbstractBottomInfobar {
    private final ClientState clientState;
    private final BottomInfobarController bottomInfobarController;

    private final JLabel connectionStatusTextLabel;
    private final JLabel connectionStatusLabel;
    private final JLabel connectionTimeLabel;
    private final JLabel lostFramesCountLabel;
    private final JLabel recvBytesPerSecLabel;

    public BottomInfobar(ClientWindow clientWindow) {
        clientState = clientWindow.getClientState();
        bottomInfobarController = new BottomInfobarController(clientWindow, this);

        connectionStatusTextLabel = new JLabel("Connection state:");
        connectionStatusLabel = new JLabel();
        connectionTimeLabel = new JLabel();
        recvBytesPerSecLabel = new JLabel();
        lostFramesCountLabel = new JLabel("Lost frames: 0");

        initObservables();

        connectionStatusTextLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
        connectionStatusLabel.setBorder(marginRight);
        connectionStatusLabel.setForeground(Color.GRAY);
        lostFramesCountLabel.setBorder(marginRight);
        
        stateCompoundPanel.add(connectionStatusTextLabel);
        stateCompoundPanel.add(connectionStatusLabel);

        leftCompoundPanel.add(stateCompoundPanel);
        leftCompoundPanel.add(connectionTimeLabel);

        rightCompoundPanel.add(memoryUsageLabel);
        rightCompoundPanel.add(lostFramesCountLabel);
        rightCompoundPanel.add(recvBytesPerSecLabel);

        addPanels();
    }

    private void initObservables() {
        clientState.wrapAsDisposable(clientState.getConnectionState$(), state -> {
            connectionStatusLabel.setForeground(state.getColor());
            connectionStatusLabel.setText(state.getState());
        });
        clientState.wrapAsDisposable(clientState.getConnectionTime$(), time -> {
            connectionTimeLabel.setText(Utils.parseTime(time, "Connection"));
        });
        clientState.wrapAsDisposable(clientState.getRecvBytesPerSec$(), bytes -> {
            recvBytesPerSecLabel.setText(Utils.parseBytesPerSecToMegaBytes(bytes, "Received"));
        });
        clientState.wrapAsDisposable(clientState.getLostFramesCount$(), lostFramesCount -> {
            lostFramesCountLabel.setText(String.format("Lost frames: %s", lostFramesCount));
        });
    }
}
