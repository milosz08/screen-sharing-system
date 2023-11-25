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
import pl.polsl.screensharing.lib.Utils;

@Data
@Builder
@AllArgsConstructor
public class FastConnectionDetails {
    private String hostIpAddress;
    private int hostPort;
    private String clientIpAddress;
    private boolean isMachineIpAddress;
    private int clientPort;
    private boolean isRandomPort;
    private String username;
    private String description;

    public FastConnectionDetails() {
        hostIpAddress = Utils.getMachineAddress();
        hostPort = SharedConstants.DEFAULT_PORT;
        clientIpAddress = Utils.getMachineAddress();
        isMachineIpAddress = true;
        clientPort = Utils.getRandomPortOrDefault(443);
        isRandomPort = true;
        username = SharedConstants.DEFAULT_USERNAME + RandomStringUtils.randomNumeric(4);
        description = StringUtils.EMPTY;
    }

    @JsonIgnore
    public void setFastConnDetails(FastConnectionDetails o) {
        hostIpAddress = o.hostIpAddress;
        hostPort = o.hostPort;
        clientIpAddress = o.isMachineIpAddress ? Utils.getMachineAddress() : o.clientIpAddress;
        isMachineIpAddress = o.isMachineIpAddress;
        clientPort = o.isRandomPort ? Utils.getRandomPortOrDefault(443) : o.clientPort;
        isRandomPort = o.isRandomPort;
        username = o.username;
        description = o.description;
    }

    @JsonIgnore
    public String getHostPortAsStr() {
        return String.valueOf(hostPort);
    }

    @JsonIgnore
    public String getClientPortAsStr() {
        return String.valueOf(getClientPort());
    }

    @JsonIgnore
    public String getClientIpAddress() {
        return isMachineIpAddress ? Utils.getMachineAddress() : clientIpAddress;
    }

    @JsonIgnore
    public int getClientPort() {
        return isRandomPort ? Utils.getRandomPortOrDefault(443) : clientPort;
    }

    @Override
    public String toString() {
        return "{" +
            "hostIpAddress=" + hostIpAddress +
            ", hostPort=" + hostPort +
            ", clientIpAddress=" + clientIpAddress +
            ", isMachineIpAddress=" + isMachineIpAddress +
            ", clientPort=" + clientPort +
            ", isRandomPort=" + isRandomPort +
            ", username=" + username +
            ", description=" + description.replaceAll("\n", StringUtils.SPACE) +
            '}';
    }
}
