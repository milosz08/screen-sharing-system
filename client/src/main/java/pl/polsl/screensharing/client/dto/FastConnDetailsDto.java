/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.dto;

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
public class FastConnDetailsDto {
    private String ipAddress;
    private int port;
    private String username;
    private String description;

    public FastConnDetailsDto() {
        this.ipAddress = SharedConstants.DEFAULT_HOST;
        this.port = SharedConstants.DEFAULT_PORT;
        this.username = SharedConstants.DEFAULT_USERNAME + RandomStringUtils.randomNumeric(4);
        this.description = StringUtils.EMPTY;
    }

    @JsonIgnore
    public void setFastConnDetailsDto(FastConnDetailsDto o) {
        this.ipAddress = o.ipAddress;
        this.port = o.port;
        this.username = o.username;
        this.description = o.description;
    }

    @JsonIgnore
    public String getPortAsStr() {
        return String.valueOf(port);
    }
}
