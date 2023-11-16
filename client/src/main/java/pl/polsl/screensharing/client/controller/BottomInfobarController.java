/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.fragment.BottomInfobar;
import pl.polsl.screensharing.lib.controller.AbstractBottomInfobarController;

import javax.swing.*;

public class BottomInfobarController extends AbstractBottomInfobarController {
    private final ClientWindow clientWindow;

    private final Timer connectionTimer;
    private final Timer recordingTimer;

    private long connectionTime;
    private long recordingTime;

    public BottomInfobarController(ClientWindow clientWindow, BottomInfobar bottomInfobar) {
        super(bottomInfobar);
        this.clientWindow = clientWindow;
        this.connectionTimer = new Timer(1000, e -> clientWindow.getClientState().updateConnectionTime(++connectionTime));
        this.recordingTimer = new Timer(1000, e -> clientWindow.getClientState().updateRecordingTime(++recordingTime));
    }

    public void startConnectionTimer() {
        connectionTimer.start();
    }

    public void stopConnectionTimer() {
        connectionTimer.stop();

        final ClientState state = clientWindow.getClientState();
        state.updateConnectionTime(0L);
        state.updateRecordingTime(0L);

        connectionTime = 0;
        recordingTime = 0;
    }

    public void startRecordingTimer() {
        recordingTimer.start();
    }

    public void stopRecordingTimer() {
        recordingTimer.stop();

        final ClientState state = clientWindow.getClientState();
        state.updateRecordingTime(0L);

        recordingTime = 0;
    }
}
