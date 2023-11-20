/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.aggregator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.polsl.screensharing.host.state.StreamingState;

@Getter
@AllArgsConstructor
public class StreamingStateFrameModeAggregator {
    private final StreamingState streamingState;
    private final boolean isFrameVisible;
}
