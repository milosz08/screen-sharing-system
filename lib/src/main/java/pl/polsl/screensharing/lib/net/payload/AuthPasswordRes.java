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

    @Override
    public String toString() {
        return "{" +
            "validStatus=" + validStatus +
            ", secretKeyUdp=******" +
            '}';
    }
}
