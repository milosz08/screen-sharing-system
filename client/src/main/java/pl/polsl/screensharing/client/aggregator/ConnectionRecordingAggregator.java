/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.aggregator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.polsl.screensharing.client.state.ConnectionState;
import pl.polsl.screensharing.client.state.RecordingState;

@Getter
@AllArgsConstructor
public class ConnectionRecordingAggregator {
    private final ConnectionState connectionState;
    private final RecordingState recordingState;
}
