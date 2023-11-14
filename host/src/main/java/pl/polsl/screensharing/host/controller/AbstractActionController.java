/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.dialog.ConnectionSettingsWindow;

@Slf4j
@RequiredArgsConstructor
abstract class AbstractActionController {
    protected final HostWindow hostWindow;

    public void openMakeConnectionWindow() {
        final ConnectionSettingsWindow window = hostWindow.getConnectionSettingsWindow();
        window.setVisible(true);
    }
}
