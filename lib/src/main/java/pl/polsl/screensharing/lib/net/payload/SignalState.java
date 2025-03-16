package pl.polsl.screensharing.lib.net.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignalState<T> {
    @JsonProperty("currentState")
    private T currentState;

    @Override
    public String toString() {
        return "{" +
            "currentState=" + currentState +
            '}';
    }
}
