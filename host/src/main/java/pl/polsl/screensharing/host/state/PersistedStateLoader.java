package pl.polsl.screensharing.host.state;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.model.FrameColorRgb;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.file.AbstractPersistorStateLoader;

@Slf4j
public class PersistedStateLoader extends AbstractPersistorStateLoader<HostState> {
    public PersistedStateLoader(HostState state) {
        super(AppType.HOST, state);
    }

    @Override
    protected void loadApplicationSavedState() {
        try {
            final PersistedState settings = objectMapper.readValue(file, PersistedState.class);
            state.updateSessionDetails(settings.getSessionDetailsWithDecryptedPassword());
            state.updateFrameColor(settings.getFrameColor().getColor());
            state.updateShowingCursorState(settings.getIsCursorShowing());
            state.updateStreamingQuality(settings.getQuality());
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

    public void persistFrameColor() {
        try {
            final PersistedState settings = objectMapper.readValue(file, PersistedState.class);
            settings.setFrameColor(new FrameColorRgb(state.getLastEmittedFrameColor()));
            objectMapper.writeValue(file, settings);
            log.info("Persist frame color: {}.", settings.getFrameColor());
        } catch (Exception ex) {
            log.error("Failure during persist frame color. Cause {}.", ex.getMessage());
        }
    }

    public void persistIsCursorShowing() {
        try {
            final PersistedState settings = objectMapper.readValue(file, PersistedState.class);
            settings.setIsCursorShowing(state.getLastEmittedIsCursorShowing());
            objectMapper.writeValue(file, settings);
            log.info("Persist is cursor showing state: {}.", settings.getIsCursorShowing());
        } catch (Exception ex) {
            log.error("Failure during persist is cursor showing state. Cause {}.", ex.getMessage());
        }
    }

    public void persistQualityLevel() {
        try {
            final PersistedState settings = objectMapper.readValue(file, PersistedState.class);
            settings.setQuality(state.getLastEmittedQualityLevel());
            objectMapper.writeValue(file, settings);
            log.info("Persist quality level state: {}.", settings.getQuality());
        } catch (Exception ex) {
            log.error("Failure during persist quality level state. Cause {}.", ex.getMessage());
        }
    }
}
