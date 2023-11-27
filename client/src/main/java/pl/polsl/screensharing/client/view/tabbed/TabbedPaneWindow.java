/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.tabbed;

import io.reactivex.rxjava3.core.Observable;
import lombok.Getter;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.fragment.JAppTabbedDataChartPanel;
import pl.polsl.screensharing.lib.gui.fragment.JAppTabbedLogsPanel;

import javax.swing.*;

@Getter
public class TabbedPaneWindow extends JTabbedPane {
    private final ClientState clientState;
    private final Observable<Long> recvBytesStream$;

    private final TabbedVideoStreamPanel tabbedVideoStreamPanel;
    private final JAppTabbedDataChartPanel tabbedDataChartPanel;
    private final JAppTabbedLogsPanel tabbedLogsPanel;

    public TabbedPaneWindow(ClientWindow clientWindow) {
        clientState = clientWindow.getClientState();
        recvBytesStream$ = clientState.getRecvBytesPerSec$();

        tabbedVideoStreamPanel = new TabbedVideoStreamPanel(clientWindow);
        tabbedDataChartPanel = new JAppTabbedDataChartPanel(clientState, recvBytesStream$);
        tabbedLogsPanel = new JAppTabbedLogsPanel(AppType.CLIENT);

        addTab("Video stream", tabbedVideoStreamPanel);
        addTab("Received data chart", tabbedDataChartPanel);
        addTab("Logs", tabbedLogsPanel);
    }
}
