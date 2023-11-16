/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.polsl.screensharing.lib.state.ColoredLabelState;

import java.awt.*;

@Getter
@RequiredArgsConstructor
public enum RecordingState implements ColoredLabelState {
    RECORDING("Recording", Color.RED),
    IDLE("Idle", Color.GRAY),
    ;

    private final String state;
    private final Color color;
}
