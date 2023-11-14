/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.state;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.client.dto.FastConnDetailsDto;
import pl.polsl.screensharing.client.dto.SavedConnDetailsDto;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.file.AbstractPersistorStateLoader;

import java.util.SortedSet;
import java.util.TreeSet;

@Slf4j
class PersistedStateLoader extends AbstractPersistorStateLoader<ClientState> {
    PersistedStateLoader(ClientState clientState) {
        super(AppType.CLIENT, clientState);
    }

    @Override
    public void loadApplicationSavedState() {
        try {
            final PersistedState settings = objectMapper.readValue(file, PersistedState.class);
            state.setFastConnDetails(settings.getFastConnection());
            state.setSavedConnDetails(settings.getSavedConnections());
            log.info("Found config file. Moved persisted settings into application context.");
        } catch (Exception ex) {
            state.setFastConnDetails(new FastConnDetailsDto());
            state.setSavedConnDetails(new TreeSet<>());
            log.warn("Cannot localized config file, skipping initialization.");
        }
    }

    void persistFastConnDetails() {
        try {
            final PersistedState settings = objectMapper.readValue(file, PersistedState.class);
            final FastConnDetailsDto connection = settings.getFastConnection();
            connection.setFastConnDetailsDto(state.getFastConnDetails());
            objectMapper.writeValue(file, settings);
            log.info("Persist connection details: {}.", state.getFastConnDetails());
        } catch (Exception ex) {
            log.error("Failure during persist connection details. Cause {}.", ex.getMessage());
        }
    }

    void persistSavedConnDetails() {
        try {
            final PersistedState settings = objectMapper.readValue(file, PersistedState.class);
            final SortedSet<SavedConnDetailsDto> persistedConnections = settings.getSavedConnections();
            persistedConnections.clear();
            persistedConnections.addAll(state.getSavedConnDetails());
            objectMapper.writeValue(file, settings);
            log.info("Persist last connections: {}.", state.getSavedConnDetails());
        } catch (Exception ex) {
            log.error("Failure during persist last connections. Cause {}.", ex.getMessage());
        }
    }
}
