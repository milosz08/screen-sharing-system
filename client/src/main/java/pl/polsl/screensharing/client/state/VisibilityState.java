package pl.polsl.screensharing.client.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.polsl.screensharing.lib.net.StreamingSignalState;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum VisibilityState {
    VISIBLE(null, StreamingSignalState.STREAMING),
    WAITING_FOR_CONNECTION("Waiting for connection with host", StreamingSignalState.STOPPED),
    TEMPORARY_HIDDEN("Screen temporary hidden by host", StreamingSignalState.SCREEN_HIDDEN),
    ;

    private final String state;
    private final StreamingSignalState streamingSignalState;

    public static VisibilityState getBasedSignalState(StreamingSignalState streamingSignalState) {
        return Arrays.stream(values())
            .filter(state -> state.streamingSignalState.equals(streamingSignalState))
            .findFirst()
            .orElse(VisibilityState.WAITING_FOR_CONNECTION);
    }
}
