/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import pl.polsl.screensharing.lib.SharedConstants;

@Data
@Builder
@AllArgsConstructor
public class FastConnectionDetails {
    private String ipAddress;
    private int port;
    private String username;
    private String description;

    public FastConnectionDetails() {
        ipAddress = SharedConstants.DEFAULT_HOST;
        port = SharedConstants.DEFAULT_PORT;
        username = SharedConstants.DEFAULT_USERNAME + RandomStringUtils.randomNumeric(4);
        description = StringUtils.EMPTY;
    }

    @JsonIgnore
    public void setFastConnDetails(FastConnectionDetails o) {
        ipAddress = o.ipAddress;
        port = o.port;
        username = o.username;
        description = o.description;
    }

    @JsonIgnore
    public String getPortAsStr() {
        return String.valueOf(port);
    }

    @Override
    public String toString() {
        return "{" +
            "ipAddress=" + ipAddress +
            ", port=" + port +
            ", username=" + username +
            ", description=" + description.replaceAll("\n", StringUtils.SPACE) +
            '}';
    }
}
