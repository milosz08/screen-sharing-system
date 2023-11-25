/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.net.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthPasswordRes {
    private boolean isValid;
    private byte[] secretKeyUdp;
    private byte[] secureRandomUdp;

    @Override
    public String toString() {
        return "{" +
            "isValid=" + isValid +
            ", secretKeyUdp=******" +
            ", secureRandomUdp==******" +
            '}';
    }
}
