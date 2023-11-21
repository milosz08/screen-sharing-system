/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.state;

import lombok.Getter;
import pl.polsl.screensharing.client.model.FastConnectionDetails;
import pl.polsl.screensharing.client.model.SavedConnection;

import java.util.SortedSet;
import java.util.TreeSet;

@Getter
class PersistedState {
    private final FastConnectionDetails fastConnection;
    private final SortedSet<SavedConnection> savedConnections;

    PersistedState() {
        fastConnection = new FastConnectionDetails();
        savedConnections = new TreeSet<>();
    }
}
