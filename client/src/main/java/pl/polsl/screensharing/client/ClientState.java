/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client;

import pl.polsl.screensharing.client.dto.ConnectionDetailsDto;
import pl.polsl.screensharing.client.dto.LastConnectionRowDto;

import java.util.ArrayList;
import java.util.List;

public class ClientState {
    private final List<LastConnectionRowDto> lastConnections;
    private final ConnectionDetailsDto connectionDetails;

    public ClientState() {
        this.lastConnections = new ArrayList<>();
        this.connectionDetails = new ConnectionDetailsDto();
    }

    public ConnectionDetailsDto getConnectionDetails() {
        return connectionDetails;
    }

    public void removeConnectionByIndex(int index) {
        if (index < 0 || index > lastConnections.size() - 1) {
            return;
        }
        final LastConnectionRowDto removingRow = lastConnections.get(index);
        lastConnections.remove(removingRow);
    }

    public void removeAllSavedConnections() {
        lastConnections.clear();
    }

    public void addNewSavedConnection(String ipv4, int port, String description) {
        final LastConnectionRowDto addingRow = new LastConnectionRowDto(ipv4, port, description);
        if (lastConnections.stream().anyMatch(row -> row.equals(addingRow))) {
            return;
        }
        lastConnections.add(addingRow);
    }

    public Object[][] getParsedLastConnectionsList() {
        return lastConnections.stream()
            .map(row -> new Object[]{ row.getIp(), row.getPort(), row.getDescription() })
            .toArray(Object[][]::new);
    }

    public void copyAndPushLastSavedConnections(List<LastConnectionRowDto> rowDtos) {
        lastConnections.clear();
        lastConnections.addAll(rowDtos);
    }
}
