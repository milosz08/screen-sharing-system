/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.net.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionData {
    private String username;
    private String ipAddress;
    private int udpPort;

    @Override
    public String toString() {
        return "{" +
            "username=" + username +
            ", ipAddress=" + ipAddress +
            ", udpPort=" + udpPort +
            '}';
    }
}
