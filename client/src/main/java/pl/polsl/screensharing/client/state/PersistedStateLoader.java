/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.state;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.file.AbstractPersistorStateLoader;

@Slf4j
public class PersistedStateLoader extends AbstractPersistorStateLoader<ClientState> {
    PersistedStateLoader(ClientState clientState) {
        super(AppType.CLIENT, clientState);
    }

    @Override
    public void loadApplicationSavedState() {
        try {
            final PersistedState settings = objectMapper.readValue(file, PersistedState.class);
            state.updateFastConnectionDetails(settings.getFastConnection());
            state.updateSavedConnections(settings.getSavedConnections());
            log.info("Found config file. Moved persisted settings into application context.");
        } catch (Exception ex) {
            log.warn("Cannot localized config file, skipping initialization.");
        }
    }

    public void persistFastConnDetails() {
        try {
            final PersistedState settings = objectMapper.readValue(file, PersistedState.class);
            settings.getFastConnection().setFastConnDetails(state.getLastEmittedFastConnectionDetails());
            objectMapper.writeValue(file, settings);
            log.info("Persist connection details: {}.", settings.getFastConnection());
        } catch (Exception ex) {
            log.error("Failure during persist connection details. Cause {}.", ex.getMessage());
        }
    }

    public void persistSavedConnDetails() {
        try {
            final PersistedState settings = objectMapper.readValue(file, PersistedState.class);
            settings.getSavedConnections().clear();
            settings.getSavedConnections().addAll(state.getLastEmittedSavedConnections());
            objectMapper.writeValue(file, settings);
            log.info("Persist last connections: {}.", settings.getSavedConnections());
        } catch (Exception ex) {
            log.error("Failure during persist last connections. Cause {}.", ex.getMessage());
        }
    }
}
