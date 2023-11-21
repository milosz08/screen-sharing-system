/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.tabbed;

import lombok.Getter;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.fragment.JAppTabbedLogsPanel;

import javax.swing.*;

@Getter
public class TabbedPaneWindow extends JTabbedPane {
    private final TabbedScreenFramePanel tabbedScreenFramePanel;
    private final JAppTabbedLogsPanel tabbedLogsPanel;

    public TabbedPaneWindow(HostWindow hostWindow) {
        tabbedScreenFramePanel = new TabbedScreenFramePanel(hostWindow);
        tabbedLogsPanel = new JAppTabbedLogsPanel(AppType.HOST);

        addTab("Screen capture", tabbedScreenFramePanel);
        addTab("Logs", tabbedLogsPanel);
    }
}
