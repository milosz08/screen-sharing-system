/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.dto;

import java.util.Objects;

public class LastConnectionRowDto {
    private final String ip;
    private final int port;
    private final String description;

    public LastConnectionRowDto(String ip, int port, String description) {
        this.ip = ip;
        this.port = port;
        this.description = description;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LastConnectionRowDto that = (LastConnectionRowDto) o;
        return port == that.port && Objects.equals(ip, that.ip) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port, description);
    }

    @Override
    public String toString() {
        return "LastConnectionRowDto{" +
            "ip='" + ip + '\'' +
            ", port=" + port +
            ", description='" + description + '\'' +
            '}';
    }
}
