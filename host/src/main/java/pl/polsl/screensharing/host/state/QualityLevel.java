/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.state;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QualityLevel {
    POOR("Poor"),
    GOOD("Good"),
    BEST("Best"),
    NO_COMPRESSION("Lossless"),
    ;

    private final String quality;

    @Override
    public String toString() {
        return quality;
    }
}
