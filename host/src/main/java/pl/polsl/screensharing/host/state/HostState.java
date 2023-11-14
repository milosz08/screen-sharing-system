/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.state;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostState {
    private boolean isSessionCreated;
    private boolean isVideoStreaming;
}
