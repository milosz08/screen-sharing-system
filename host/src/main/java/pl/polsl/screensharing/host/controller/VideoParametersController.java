/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.dialog.ParticipantsDialogWindow;

@Slf4j
public class VideoParametersController extends AbstractStreamController {
    public VideoParametersController(HostWindow hostWindow) {
        super(hostWindow);
    }

    public void showParticipantsDialog() {
        final ParticipantsDialogWindow window = hostWindow.getParticipantsDialogWindow();
        window.setVisible(true);
    }
}
