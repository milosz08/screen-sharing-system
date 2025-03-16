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
public class AuthPasswordReq {
    @JsonProperty("password")
    private String password;

    @Override
    public String toString() {
        return "{" +
            "password=" + password +
            '}';
    }
}
