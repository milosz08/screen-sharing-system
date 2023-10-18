/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.polsl.screensharing.client.dto.ConnectionDetailsDto;
import pl.polsl.screensharing.client.dto.LastConnectionRowDto;

import java.util.List;

public class ClientState {
    private static final Logger LOG = LoggerFactory.getLogger(ClientState.class);

    private final PersistedStateLoader persistedStateLoader;

    private List<LastConnectionRowDto> lastConnections;
    private ConnectionDetailsDto connectionDetails;
    private boolean isConnectionEstabilished;

    public ClientState() {
        this.persistedStateLoader = new PersistedStateLoader(this);
        this.persistedStateLoader.initPersistor(new SavedStateDto());
        this.persistedStateLoader.loadApplicationSavedState();
    }

    public void removeConnectionByIndex(int index) {
        if (index >= 0 && index < lastConnections.size()) {
            final LastConnectionRowDto removingRow = lastConnections.get(index);
            lastConnections.remove(removingRow);
            LOG.info("Removed last connection: {}.", removingRow);
            persistedStateLoader.persistLastConnectionsState();
        }
    }

    public void removeAllSavedConnections() {
        lastConnections.clear();
        LOG.info("Removed all saved last connections.");
        persistedStateLoader.persistLastConnectionsState();
    }

    public boolean addNewSavedConnection(String ipv4, int port, String username, String description) {
        final LastConnectionRowDto addingRow = new LastConnectionRowDto(ipv4, port, username, description);
        if (lastConnections.stream().noneMatch(row -> row.equals(addingRow))) {
            lastConnections.add(addingRow);
            LOG.info("Add new last connection: {}.", addingRow);
            persistedStateLoader.persistLastConnectionsState();
            return false;
        }
        LOG.info("Connection {} already exist. Skipping.", addingRow);
        return true;
    }

    public Object[][] getParsedLastConnectionsList() {
        return lastConnections.stream()
            .map(row -> new Object[]{ row.getIpv4(), row.getPort(), row.getUsername(), row.getDescription() })
            .toArray(Object[][]::new);
    }

    public void copyAndPushLastSavedConnections(List<LastConnectionRowDto> rowDtos) {
        lastConnections.clear();
        lastConnections.addAll(rowDtos);
        LOG.info("Update last connections table rows. Updated table: {}.", rowDtos);
        persistedStateLoader.persistLastConnectionsState();
    }

    public ConnectionDetailsDto getConnectionDetails() {
        return connectionDetails;
    }

    public List<LastConnectionRowDto> getLastConnections() {
        return lastConnections;
    }

    public void persistConnectionDetails(ConnectionDetailsDto detailsDto) {
        connectionDetails.setConnectionDetails(detailsDto);
        LOG.info("Update connection details: {}.", detailsDto);
        persistedStateLoader.persistConnectionDetailsState();
    }

    public void setConnectionEstabilished(boolean isConnectionEstabilished) {
        this.isConnectionEstabilished = isConnectionEstabilished;
    }

    void setConnectionDetails(ConnectionDetailsDto connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    void setLastConnections(List<LastConnectionRowDto> lastConnections) {
        this.lastConnections = lastConnections;
    }
}
