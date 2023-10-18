/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.dto;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class ConnectionDetailsDto {
    private SocketAddress ipv4;
    private String username;
    private String password;

    public void setConnectionDetails(String ipv4, int port) {
        this.ipv4 = new InetSocketAddress(ipv4, port);
    }

    public void setAuthConnectionDetails(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public SocketAddress getIpv4() {
        return ipv4;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "ConnectionDetailsDto{" +
            "ipv4=" + ipv4 +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
