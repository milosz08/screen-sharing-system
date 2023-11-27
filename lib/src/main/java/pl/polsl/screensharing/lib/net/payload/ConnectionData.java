/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.net.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionData {
    @JsonProperty("username")
    private String username;

    @JsonProperty("ipAddress")
    private String ipAddress;

    @JsonProperty("udpPort")
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
