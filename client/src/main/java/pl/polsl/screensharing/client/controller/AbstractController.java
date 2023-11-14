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
        topToolbar.getStartRecordingButton().setEnabled(isConnected);
        topToolbar.getStopRecordingButton().setEnabled(isConnected);
        topToolbar.getTakeScreenshotButton().setEnabled(isConnected);

        topMenuBar.getCreateConnectionMenuItem().setEnabled(!isConnected);
        topMenuBar.getLastConnectionsMenuItem().setEnabled(!isConnected);
        topMenuBar.getDisconnectMenuItem().setEnabled(isConnected);
        topMenuBar.getStartRecordingMenuItem().setEnabled(isConnected);
        topMenuBar.getStopRecordingMenuItem().setEnabled(isConnected);
        topMenuBar.getTakeScreenshotMenuItem().setEnabled(isConnected);
    }

    protected void updateRecordingButtonsState(boolean isRecording) {
        final TopToolbar topToolbar = clientWindow.getTopToolbar();
        final TopMenuBar topMenuBar = clientWindow.getTopMenuBar();

        topToolbar.getStartRecordingButton().setEnabled(!isRecording);
        topToolbar.getStopRecordingButton().setEnabled(isRecording);

        topMenuBar.getStartRecordingMenuItem().setEnabled(!isRecording);
        topMenuBar.getStopRecordingMenuItem().setEnabled(isRecording);
    }
}
