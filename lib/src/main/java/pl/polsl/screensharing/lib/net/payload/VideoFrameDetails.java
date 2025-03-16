package pl.polsl.screensharing.lib.net.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.polsl.screensharing.lib.net.StreamingSignalState;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoFrameDetails {
    @JsonProperty("aspectRatio")
    private double aspectRatio;

    @JsonProperty("streamingSignalState")
    private StreamingSignalState streamingSignalState;

    @Override
    public String toString() {
        return "{" +
            "aspectRatio=" + aspectRatio +
            ", streamingSignalState=" + streamingSignalState +
            '}';
    }
}
