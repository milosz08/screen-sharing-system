/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.tabbed;

import io.reactivex.rxjava3.core.Observable;
import lombok.Getter;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.fragment.JAppTabbedDataChartPanel;
import pl.polsl.screensharing.lib.gui.fragment.JAppTabbedLogsPanel;

import javax.swing.*;

@Getter
public class TabbedPaneWindow extends JTabbedPane {
    private final HostState hostState;
    private final Observable<Long> sendBytesStream$;

    private final TabbedScreenFramePanel tabbedScreenFramePanel;
    private final JAppTabbedDataChartPanel tabbedChartPanel;
    private final JAppTabbedLogsPanel tabbedLogsPanel;

    public TabbedPaneWindow(HostWindow hostWindow) {
        hostState = hostWindow.getHostState();
        sendBytesStream$ = hostState.getSendBytesPerSec$();

        tabbedScreenFramePanel = new TabbedScreenFramePanel(hostWindow);
        tabbedChartPanel = new JAppTabbedDataChartPanel(hostState, sendBytesStream$);
        tabbedLogsPanel = new JAppTabbedLogsPanel(AppType.HOST);

        addTab("Screen capture", tabbedScreenFramePanel);
        addTab("Sended data chart", tabbedChartPanel);
        addTab("Logs", tabbedLogsPanel);
    }
}
