/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import pl.polsl.screensharing.client.model.FastConnectionDetails;
import pl.polsl.screensharing.client.model.SavedConnection;

import java.util.SortedSet;
import java.util.TreeSet;

@Getter
class PersistedState {
    @JsonProperty("fastConnection")
    private final FastConnectionDetails fastConnection;

    @JsonProperty("savedConnections")
    private final SortedSet<SavedConnection> savedConnections;

    PersistedState() {
        fastConnection = new FastConnectionDetails();
        savedConnections = new TreeSet<>();
    }
}
