/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.state;

import lombok.Getter;
import pl.polsl.screensharing.client.dto.FastConnDetailsDto;
import pl.polsl.screensharing.client.dto.SavedConnDetailsDto;

import java.util.SortedSet;
import java.util.TreeSet;

@Getter
class PersistedState {
    private final FastConnDetailsDto fastConnection;
    private final SortedSet<SavedConnDetailsDto> savedConnections;

    PersistedState() {
        this.fastConnection = new FastConnDetailsDto();
        this.savedConnections = new TreeSet<>();
    }
}
