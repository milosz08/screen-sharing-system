/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.fragment;

import io.reactivex.rxjava3.core.Observable;
import lombok.Getter;
import pl.polsl.screensharing.client.aggregator.ConnectionRecordingAggregator;
import pl.polsl.screensharing.client.controller.TopToolbarController;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.state.ConnectionState;
import pl.polsl.screensharing.client.state.RecordingState;
import pl.polsl.screensharing.client.view.ClientIcon;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.gui.component.JAppIconButton;
import pl.polsl.screensharing.lib.gui.icon.LibIcon;

import javax.swing.*;

@Getter
public class TopToolbar extends JToolBar {
    private final ClientState clientState;

    private final JAppIconButton createConnectionButton;
    private final JAppIconButton disconnectButton;
    private final JAppIconButton lastConnectionsButton;
    private final JAppIconButton takeScreenshotButton;
    private final JAppIconButton startRecordingButton;
    private final JAppIconButton stopRecordingButton;

    private final TopToolbarController controller;

    public TopToolbar(ClientWindow clientWindow) {
        this.clientState = clientWindow.getClientState();
        this.controller = new TopToolbarController(clientWindow);

        this.createConnectionButton = new JAppIconButton("Connect", ClientIcon.ADD_CONNECTION, true);
        this.disconnectButton = new JAppIconButton("Disconnect", ClientIcon.DISCONNECT, true, false);
        this.lastConnectionsButton = new JAppIconButton("Last connections", LibIcon.CHECK_BOX_LIST, true);

        this.takeScreenshotButton = new JAppIconButton("Take screenshot", ClientIcon.SCREENSHOT, true, false);
        this.startRecordingButton = new JAppIconButton("Start recording", ClientIcon.RECORD, true, false);
        this.stopRecordingButton = new JAppIconButton("Stop recording", ClientIcon.STOP, true, false);

        initObservables();

        this.createConnectionButton.addActionListener(e -> controller.openMakeConnectionWindow());
        this.disconnectButton.addActionListener(e -> controller.disconnectFromSession());
        this.lastConnectionsButton.addActionListener(e -> controller.openLastConnectionsWindow());

        this.takeScreenshotButton.addActionListener(e -> controller.takeScreenshot());
        this.startRecordingButton.addActionListener(e -> controller.startRecording());
        this.stopRecordingButton.addActionListener(e -> controller.stopRecording());

        addButtonWithSeparation(createConnectionButton);
        addButtonWithSeparation(disconnectButton);
        addButtonWithSeparation(lastConnectionsButton);
        addSeparator();
        addButtonWithSeparation(takeScreenshotButton);
        addButtonWithSeparation(startRecordingButton);
        addButtonWithSeparation(stopRecordingButton);

        setFloatable(false);
    }

    private void addButtonWithSeparation(JAppIconButton button) {
        add(button);
        add(Box.createHorizontalStrut(5));
    }

    private void initObservables() {
        clientState.wrapAsDisposable(clientState.getConnectionState$(), state -> {
            final boolean isConnected = state.equals(ConnectionState.CONNECTED);
            createConnectionButton.setEnabled(!isConnected);
            lastConnectionsButton.setEnabled(!isConnected);
            disconnectButton.setEnabled(isConnected);
            takeScreenshotButton.setEnabled(isConnected);
        });

        final Observable<ConnectionRecordingAggregator> aggregator = Observable.combineLatest(
            clientState.getConnectionState$(),
            clientState.getRecordingState$(),
            ConnectionRecordingAggregator::new);

        clientState.wrapAsDisposable(aggregator, state -> {
            final boolean isCreated = state.getConnectionState().equals(ConnectionState.CONNECTED);
            final boolean isStreaming = state.getRecordingState().equals(RecordingState.RECORDING);
            startRecordingButton.setEnabled(!isStreaming && isCreated);
            stopRecordingButton.setEnabled(isStreaming && isCreated);
        });
    }
}
