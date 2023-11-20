/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QualityLevel {
    POOR("Poor", 0.2f),
    GOOD("Good", 0.6f),
    BEST("Best", 0.9f),
    ;

    private final String quality;
    @Getter
    private final float jpegLevel;

    @Override
    public String toString() {
        return quality;
    }
}
