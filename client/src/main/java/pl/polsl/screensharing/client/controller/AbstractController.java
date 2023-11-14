/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.controller;

import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.client.view.fragment.TopMenuBar;
import pl.polsl.screensharing.client.view.fragment.TopToolbar;

abstract class AbstractController {
    protected final ClientWindow clientWindow;

    AbstractController(ClientWindow clientWindow) {
        this.clientWindow = clientWindow;
    }

    protected void updateConnectionButtonsState(boolean isConnected) {
        final TopToolbar topToolbar = clientWindow.getTopToolbar();
        final TopMenuBar topMenuBar = clientWindow.getTopMenuBar();

        topToolbar.getCreateConnectionButton().setEnabled(!isConnected);
        topToolbar.getLastConnectionsButton().setEnabled(!isConnected);
        topToolbar.getDisconnectButton().setEnabled(isConnected);

        topMenuBar.getCreateConnectionMenuItem().setEnabled(!isConnected);
        topMenuBar.getLastConnectionsMenuItem().setEnabled(!isConnected);
        topMenuBar.getDisconnectMenuItem().setEnabled(isConnected);

        updateRecordingButtonsState(false, isConnected);
    }

    protected void updateRecordingButtonsState(boolean isRecording, boolean isConnected) {
        final TopToolbar topToolbar = clientWindow.getTopToolbar();
        final TopMenuBar topMenuBar = clientWindow.getTopMenuBar();

        topToolbar.getStartRecordingButton().setEnabled(!isRecording && isConnected);
        topToolbar.getStopRecordingButton().setEnabled(isRecording && isConnected);

        topMenuBar.getStartRecordingMenuItem().setEnabled(!isRecording && isConnected);
        topMenuBar.getStopRecordingMenuItem().setEnabled(isRecording && isConnected);
    }
}