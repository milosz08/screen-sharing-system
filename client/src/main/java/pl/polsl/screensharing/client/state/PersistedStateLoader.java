/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.state;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.client.dto.ConnectionDetailsDto;
import pl.polsl.screensharing.client.dto.LastConnectionRowDto;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.file.AbstractPersistorStateLoader;

import java.util.ArrayList;
import java.util.List;

@Slf4j
class PersistedStateLoader extends AbstractPersistorStateLoader<ClientState> {
    PersistedStateLoader(ClientState clientState) {
        super(AppType.CLIENT, clientState);
    }

    @Override
    public void loadApplicationSavedState() {
        try {
            final SavedStateDto settings = objectMapper.readValue(file, SavedStateDto.class);
            state.setConnectionDetails(settings.getConnectionDetails());
            state.setLastConnections(settings.getLastConnections());
            log.info("Found config file. Moved persisted settings into application context.");
        } catch (Exception ex) {
            state.setConnectionDetails(new ConnectionDetailsDto());
            state.setLastConnections(new ArrayList<>());
            log.warn("Cannot localized config file, skipping initialization.");
        }
    }

    void persistConnectionDetailsState() {
        try {
            final SavedStateDto settings = objectMapper.readValue(file, SavedStateDto.class);
            final ConnectionDetailsDto connection = settings.getConnectionDetails();
            connection.updateConnectionDetails(state.getConnectionDetails());
            objectMapper.writeValue(file, settings);
            log.info("Persist connection details: {}.", state.getConnectionDetails());
        } catch (Exception ex) {
            log.error("Failure during persist connection details. Cause {}.", ex.getMessage());
        }
    }

    void persistLastConnectionsState() {
        try {
            final SavedStateDto settings = objectMapper.readValue(file, SavedStateDto.class);
            final List<LastConnectionRowDto> persistedConnections = settings.getLastConnections();
            persistedConnections.clear();
            persistedConnections.addAll(state.getLastConnections());
            objectMapper.writeValue(file, settings);
            log.info("Persist last connections: {}.", state.getLastConnections());
        } catch (Exception ex) {
            log.error("Failure during persist last connections. Cause {}.", ex.getMessage());
        }
    }
}
