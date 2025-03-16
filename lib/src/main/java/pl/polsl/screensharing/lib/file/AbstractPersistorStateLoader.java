package pl.polsl.screensharing.lib.file;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.lib.AppType;

import java.io.File;

@Slf4j
public abstract class AbstractPersistorStateLoader<T> {
    protected final ObjectMapper objectMapper;
    protected final File file;
    protected final T state;

    public AbstractPersistorStateLoader(AppType type, T state) {
        this.state = state;
        file = new File(type.getConfigFileName());
        objectMapper = new ObjectMapper();

        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public <E> void initPersistor(E initStateWrapper) {
        try {
            if (file.exists()) {
                return;
            }
            final boolean isCreated = file.createNewFile();
            if (isCreated) {
                objectMapper.writeValue(file, initStateWrapper);
                log.info("Init values was successfully written.");
            }
        } catch (Exception ex) {
            log.error("Failure during writing init values. Cause {}.", ex.getMessage());
        }
    }

    protected abstract void loadApplicationSavedState();
}
