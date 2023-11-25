/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.net;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ConnectedClientInfo {
    private final ClientThread clientThread;
    private final String username;
    private final String ipAddress;
    private final int udpPort;
}
