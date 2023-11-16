/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.fragment.BottomInfobar;
import pl.polsl.screensharing.lib.Parser;
import pl.polsl.screensharing.lib.controller.AbstractBottomInfobarController;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class BottomInfobarController extends AbstractBottomInfobarController {
    private final HostWindow hostWindow;
    private final BottomInfobar bottomInfobar;

    private final Timer sessionTimer;
    private final Timer streamingTimer;

    private final Consumer<HostState> onSessionTimerListener = state -> {
        state.increaseSessionTime();
        updateSessionTimerUI();
    };

    private final Consumer<HostState> onStreamingTimerListener = state -> {
        state.increaseStreamingTime();
        updateStreamingTimerUI();
    };

    public BottomInfobarController(HostWindow hostWindow, BottomInfobar bottomInfobar) {
        super(bottomInfobar);
        this.hostWindow = hostWindow;
        this.bottomInfobar = bottomInfobar;
        this.sessionTimer = new Timer(1000, e -> onSessionTimerListener.accept(hostWindow.getHostState()));
        this.streamingTimer = new Timer(1000, e -> onStreamingTimerListener.accept(hostWindow.getHostState()));
    }

    public void updateSessionStateUi() {
        final HostState state = hostWindow.getHostState();
        updatePrimaryStateUi(state.getSessionState(), bottomInfobar.getSessionStatusLabel());
    }

    public void startSessionTimer() {
        sessionTimer.start();
    }

    public void stopSessionTimer() {
        streamingTimer.stop();
        sessionTimer.stop();

        final HostState state = hostWindow.getHostState();
        state.setSessionTime(0);
        state.setStreamingTime(0);

        updateSessionTimerUI();
        updateStreamingTimerUI();
    }

    public void startStreamingTimer() {
        streamingTimer.start();
        bottomInfobar.getStreamingRectInfo().setBackground(Color.RED);
    }

    public void stopStreamingTimer() {
        streamingTimer.stop();

        final HostState state = hostWindow.getHostState();
        state.setStreamingTime(0);

        bottomInfobar.getStreamingRectInfo().setBackground(Color.GRAY);
        updateStreamingTimerUI();
    }

    private void updateSessionTimerUI() {
        final HostState state = hostWindow.getHostState();
        bottomInfobar.getSessionTimeLabel().setText(Parser.parseTime(state.getSessionTime(), "Session"));
    }

    private void updateStreamingTimerUI() {
        final HostState state = hostWindow.getHostState();
        bottomInfobar.getStreamingTimeLabel().setText(Parser.parseTime(state.getStreamingTime(), "Streaming"));
    }
}
