/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VisibilityState {
    VISIBLE(null),
    WAITING_FOR_CONNECTION("Waiting for connection with host"),
    TEMPORARY_HIDDEN("Screen temporary hidden by host");

    private final String state;
}
