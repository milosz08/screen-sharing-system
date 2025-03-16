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
