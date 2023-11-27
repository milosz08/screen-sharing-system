/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.net.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthPasswordRes {
    @JsonProperty("validStatus")
    private boolean validStatus;

    @JsonProperty("secretKeyUdp")
    private byte[] secretKeyUdp;

    @JsonProperty("secureRandomUdp")
    private byte[] secureRandomUdp;

    @Override
    public String toString() {
        return "{" +
            "validStatus=" + validStatus +
            ", secretKeyUdp=******" +
            ", secureRandomUdp==******" +
            '}';
    }
}
