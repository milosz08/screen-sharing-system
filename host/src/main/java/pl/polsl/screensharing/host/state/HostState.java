/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.state;

import lombok.Getter;
import lombok.Setter;
import pl.polsl.screensharing.lib.state.IntegrityProvider;

@Getter
@Setter
public class HostState implements IntegrityProvider {
    private StreamingState streamingState;
    private SessionState sessionState;
    private long sessionTime;
    private long streamingTime;
    private long sendBytesPerSec;
    private int streamingFps;

    public HostState() {
        this.streamingState = StreamingState.STOPPED;
        this.sessionState = SessionState.INACTIVE;
        this.streamingFps = 15;
    }

    public boolean isSessionCreated() {
        return sessionState.equals(SessionState.CREATED);
    }

    public void increaseSessionTime() {
        sessionTime++;
    }

    public void increaseStreamingTime() {
        streamingTime++;
    }

    @Override
    public boolean isIntegrityStateTrue() {
        return streamingState.equals(StreamingState.STREAMING);
    }
}
