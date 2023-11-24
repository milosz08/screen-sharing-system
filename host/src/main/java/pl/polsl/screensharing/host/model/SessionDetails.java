/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String ipAddress;
    private boolean isMachineIp;
    private int port;
    private boolean hasPassword;
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
        password = new String(Base64.getDecoder().decode(o.password));
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
