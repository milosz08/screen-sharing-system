/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.state;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.file.AbstractPersistorStateLoader;

@Slf4j
public class PersistedStateLoader extends AbstractPersistorStateLoader<HostState> {
    public PersistedStateLoader(HostState state) {
        super(AppType.HOST, state);
    }

    @Override
    protected void loadApplicationSavedState() {
        try {
            final PersistedState settings = objectMapper.readValue(file, PersistedState.class);
            state.updateSessionDetails(settings.getSessionDetails());
            log.info("Found config file. Moved persisted settings into application context.");
        } catch (Exception ex) {
            log.warn("Cannot localized config file, skipping initialization.");
        }
    }

    public void persistSessionDetails() {
        try {
            final PersistedState settings = objectMapper.readValue(file, PersistedState.class);
            settings.getSessionDetails().setSessionDetails(state.getLastEmittedSessionDetails());
            objectMapper.writeValue(file, settings);
            log.info("Persist session details: {}.", settings.getSessionDetails());
        } catch (Exception ex) {
            log.error("Failure during persist session details. Cause {}.", ex.getMessage());
        }
    }
}
