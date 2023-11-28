/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import pl.polsl.screensharing.lib.SharedConstants;
import pl.polsl.screensharing.lib.Utils;

import java.util.Base64;

@Data
@Builder
@AllArgsConstructor
public class SessionDetails {
    @JsonProperty("ipAddress")
    private String ipAddress;

    @JsonProperty("isMachineIp")
    private Boolean isMachineIp;

    @JsonProperty("port")
    private int port;

    @JsonProperty("hasPassword")
    private Boolean hasPassword;

    @JsonProperty("password")
    private String password;

    public SessionDetails() {
        ipAddress = Utils.getMachineAddress();
        isMachineIp = true;
        port = SharedConstants.DEFAULT_PORT;
        hasPassword = false;
        password = StringUtils.EMPTY;
    }

    @JsonIgnore
    public void setSessionDetails(SessionDetails o) {
        ipAddress = o.ipAddress;
        isMachineIp = o.isMachineIp;
        port = o.port;
        hasPassword = o.hasPassword;
        password = Base64.getEncoder().encodeToString(o.password.getBytes());
    }

    @JsonIgnore
    public String getPortAsStr() {
        return String.valueOf(port);
    }

    @Override
    public String toString() {
        return "{" +
            "ipAddress=" + ipAddress +
            ", isMachineIp=" + isMachineIp +
            ", port=" + port +
            ", hasPassword=" + hasPassword +
            ", password=******" +
            '}';
    }
}
