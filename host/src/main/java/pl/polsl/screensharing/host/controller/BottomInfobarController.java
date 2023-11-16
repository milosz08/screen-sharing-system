/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.fragment.BottomInfobar;
import pl.polsl.screensharing.lib.controller.AbstractBottomInfobarController;

import javax.swing.*;

public class BottomInfobarController extends AbstractBottomInfobarController {
    private final HostWindow hostWindow;

    private final Timer sessionTimer;
    private final Timer streamingTimer;

    private long sessionTime;
    private long streamingTime;

    public BottomInfobarController(HostWindow hostWindow, BottomInfobar bottomInfobar) {
        super(bottomInfobar);
        this.hostWindow = hostWindow;
        this.sessionTimer = new Timer(1000, e -> hostWindow.getHostState().updateSessionTime(++sessionTime));
        this.streamingTimer = new Timer(1000, e -> hostWindow.getHostState().updateStreamingTime(++streamingTime));
    }

    public void startSessionTimer() {
        sessionTimer.start();
    }

    public void stopSessionTimer() {
        streamingTimer.stop();
        sessionTimer.stop();

        final HostState state = hostWindow.getHostState();
        state.updateSessionTime(0L);
        state.updateStreamingTime(0L);

        sessionTime = 0;
        streamingTime = 0;
    }

    public void startStreamingTimer() {
        streamingTimer.start();
    }

    public void stopStreamingTimer() {
        streamingTimer.stop();

        final HostState state = hostWindow.getHostState();
        state.updateStreamingTime(0L);

        streamingTime = 0;
    }
}
