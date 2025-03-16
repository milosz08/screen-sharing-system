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
