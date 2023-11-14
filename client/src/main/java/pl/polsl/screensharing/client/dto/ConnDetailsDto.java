/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnDetailsDto {
    private String ipAddress;
    private int port;
    private String username;
    private String password;
}
