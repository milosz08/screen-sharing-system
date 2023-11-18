/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.aggregator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.polsl.screensharing.host.state.CaptureMode;

@Getter
@AllArgsConstructor
public class CapturedModeFrameAggregator {
    private final CaptureMode captureMode;
    private final boolean isShowingFrame;
}
