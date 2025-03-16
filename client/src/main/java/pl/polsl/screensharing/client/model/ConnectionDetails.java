package pl.polsl.screensharing.client.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectionDetails {
    private String hostIpAddress;
    private int hostPort;
    private String clientIpAddress;
    private int clientPort;
    private String username;
    private String password;

    @Override
    public String toString() {
        return "{" +
            "hostIpAddress=" + hostIpAddress +
            ", hostPort=" + hostPort +
            ", clientIpAddress=" + clientIpAddress +
            ", clientPort=" + clientPort +
            ", username=" + username +
            ", password=******" +
            '}';
    }
}
