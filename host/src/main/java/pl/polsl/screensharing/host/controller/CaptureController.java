/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.view.CaptureFramelessWindow;
import pl.polsl.screensharing.host.view.HostWindow;

import javax.swing.*;
import java.awt.*;

@Slf4j
@RequiredArgsConstructor
public class CaptureController {
    private final HostWindow hostWindow;
    private final CaptureFramelessWindow captureFramelessWindow;

    public void toggleFrameBorder(boolean isActive) {
        final JPanel mainJpanel = captureFramelessWindow.getCenterTranslucentFrame();
        Color color = Color.GREEN;
        if (isActive) {
            color = Color.RED;
        }
        mainJpanel.setBorder(BorderFactory.createLineBorder(color, 3));
    }
}
