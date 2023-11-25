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
public class VideoFrameDetails {
    private double aspectRatio;
    private boolean isStreaming;

    @Override
    public String toString() {
        return "{" +
            "aspectRatio=" + aspectRatio +
            ", isStreaming=" + isStreaming +
            '}';
    }
}
