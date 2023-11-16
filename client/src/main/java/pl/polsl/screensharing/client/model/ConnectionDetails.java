/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectionDetails {
    private String ipAddress;
    private int port;
    private String username;
    private String password;
}
