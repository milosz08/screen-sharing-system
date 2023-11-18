/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CaptureMode {
    FULL_FRAME("Capture full screen"),
    AREA("Capture selected area"),
    ;

    private final String state;
}
