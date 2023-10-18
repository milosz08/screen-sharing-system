/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.dto;

import java.util.Objects;

public class LastConnectionRowDto {
    private String ipv4;
    private int port;
    private String username;
    private String description;

    public LastConnectionRowDto() {
    }

    public LastConnectionRowDto(String ipv4, int port, String username, String description) {
        this.ipv4 = ipv4;
        this.port = port;
        this.username = username;
        this.description = description;
    }

    public String getIpv4() {
        return ipv4;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LastConnectionRowDto that = (LastConnectionRowDto) o;
        return port == that.port && Objects.equals(ipv4, that.ipv4) && Objects.equals(description, that.description)
            && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipv4, port, description, username);
    }

    @Override
    public String toString() {
        return "LastConnectionRowDto{" +
            "ipv4='" + ipv4 + '\'' +
            ", port=" + port +
            ", description='" + description + '\'' +
            ", username='" + username + '\'' +
            '}';
    }
}
