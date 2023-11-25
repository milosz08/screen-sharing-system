/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SavedConnection implements Comparable<SavedConnection> {
    private int id;
    private String hostIpAddress;
    private int hostPort;
    private String clientIpAddress;
    private int clientPort;
    private String username;
    private String description;

    @Override
    public int compareTo(SavedConnection o) {
        return Integer.compare(id, o.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SavedConnection that = (SavedConnection) o;
        return hostPort == that.hostPort && Objects.equals(hostIpAddress, that.hostIpAddress)
            && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return hostIpAddress.hashCode() * hostPort * username.hashCode() * description.hashCode();
    }

    @Override
    public String toString() {
        return "{" +
            "id=" + id +
            ", hostIpAddress=" + hostIpAddress +
            ", hostPort=" + hostPort +
            ", clientIpAddress=" + clientIpAddress +
            ", clientPort=" + clientPort +
            ", username=" + username +
            ", description=" + description.replaceAll("\n", StringUtils.SPACE) +
            '}';
    }
}
