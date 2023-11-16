/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.fragment.BottomInfobar;
import pl.polsl.screensharing.lib.Parser;
import pl.polsl.screensharing.lib.controller.AbstractBottomInfobarController;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class BottomInfobarController extends AbstractBottomInfobarController {
    private final ClientWindow clientWindow;
    private final BottomInfobar bottomInfobar;

    private final Timer connectionTimer;
    private final Timer recordingTimer;

    private final Consumer<ClientState> onConnectionTimerListener = state -> {
        state.increaseConnectionTime();
        updateConnectionTimerUI();
    };

    private final Consumer<ClientState> onRecordingTimerListener = state -> {
        state.increaseRecordingTime();
        updateRecordingTimerUI();
    };

    public BottomInfobarController(ClientWindow clientWindow, BottomInfobar bottomInfobar) {
        super(bottomInfobar);
        this.clientWindow = clientWindow;
        this.bottomInfobar = bottomInfobar;
        this.connectionTimer = new Timer(1000, e -> onConnectionTimerListener.accept(clientWindow.getClientState()));
        this.recordingTimer = new Timer(1000, e -> onRecordingTimerListener.accept(clientWindow.getClientState()));
    }

    public void updateConnectionStateUi() {
        final ClientState state = clientWindow.getClientState();
        updatePrimaryStateUi(state.getConnectionState(), bottomInfobar.getConnectionStatusLabel());
    }

    public void startConnectionTimer() {
        connectionTimer.start();
    }

    public void stopConnectionTimer() {
        final ClientState state = clientWindow.getClientState();
        state.setConnectionTime(0);
        state.setRecordingTime(0);

        connectionTimer.stop();

        updateConnectionTimerUI();
        updateRecordingTimerUI();
    }

    public void startRecordingTimer() {
        recordingTimer.start();
        bottomInfobar.getRecordingRectInfo().setBackground(Color.RED);
    }

    public void stopRecordingTimer() {
        final ClientState state = clientWindow.getClientState();
        state.setRecordingTime(0);

        recordingTimer.stop();
        bottomInfobar.getRecordingRectInfo().setBackground(Color.GRAY);

        updateRecordingTimerUI();
    }

    private void updateConnectionTimerUI() {
        final ClientState state = clientWindow.getClientState();
        bottomInfobar.getConnectionTimeLabel().setText(Parser.parseTime(state.getConnectionTime(), "Connection"));
    }

    private void updateRecordingTimerUI() {
        final ClientState state = clientWindow.getClientState();
        bottomInfobar.getRecordingTimeLabel().setText(Parser.parseTime(state.getRecordingTime(), "Recording"));
    }
}
