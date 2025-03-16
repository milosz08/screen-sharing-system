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
