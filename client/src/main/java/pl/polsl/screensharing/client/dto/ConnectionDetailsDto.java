/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import pl.polsl.screensharing.lib.SharedConstants;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class ConnectionDetailsDto {
    private InetSocketAddress ipv4;
    private String username;
    private String description;

    public ConnectionDetailsDto() {
        this.ipv4 = new InetSocketAddress(SharedConstants.DEFAULT_HOST, 9091);
        this.username = parseLocalMachineAddress();
        this.description = StringUtils.EMPTY;
    }

    public ConnectionDetailsDto(String ipv4, int port, String username, String description) {
        this.ipv4 = new InetSocketAddress(ipv4, port);
        this.username = username;
        this.description = description;
    }

    public void setConnectionDetails(ConnectionDetailsDto detailsDto) {
        this.ipv4 = detailsDto.ipv4;
        this.username = detailsDto.username;
        this.description = detailsDto.description;
    }

    public void updateConnectionDetails(ConnectionDetailsDto connectionDetailsDto) {
        this.ipv4 = connectionDetailsDto.ipv4;
        this.username = connectionDetailsDto.username;
        this.description = connectionDetailsDto.description;
    }

    public InetSocketAddress getIpv4() {
        return ipv4;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    @JsonIgnore
    public String getHostAddress() {
        return ipv4.getAddress().getHostAddress();
    }

    private String parseLocalMachineAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return SharedConstants.DEFAULT_USERNAME;
        }
    }

    @Override
    public String toString() {
        return "ConnectionDetailsDto{" +
            "ipv4=" + ipv4 +
            ", username='" + username + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
