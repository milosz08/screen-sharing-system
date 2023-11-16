/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.aggregator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.polsl.screensharing.host.state.SessionState;
import pl.polsl.screensharing.host.state.StreamingState;

@Getter
@AllArgsConstructor
public class SessionStreamingAggregator {
    private final SessionState sessionState;
    private final StreamingState streamingState;
}
