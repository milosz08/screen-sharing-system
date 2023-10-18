/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.file;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.polsl.screensharing.lib.AppType;

import java.io.File;

public abstract class AbstractPersistorStateLoader<T> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractPersistorStateLoader.class);

    protected final ObjectMapper objectMapper;
    protected final File file;
    protected final T state;

    public AbstractPersistorStateLoader(AppType type, T state) {
        this.state = state;
        this.file = new File(type.getConfigFileName());
        this.objectMapper = new ObjectMapper();

        this.objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public <E> void initPersistor(E initStateWrapper) {
        try {
            if (file.exists()) {
                return;
            }
            final boolean isCreated = file.createNewFile();
            if (isCreated) {
                objectMapper.writeValue(file, initStateWrapper);
                LOG.info("Init values was successfully written.");
            }
        } catch (Exception ex) {
            LOG.error("Failure during writing init values. Cause {}.", ex.getMessage());
        }
    }

    protected abstract void loadApplicationSavedState();
}
