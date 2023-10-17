/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib;

import pl.polsl.screensharing.lib.gui.file.FileUtils;

import java.awt.*;
import java.util.Optional;

public enum AppType {
    HOST("HOST", "HostIcon", new Dimension(1280, 720)),
    CLIENT("CLIENT", "ClientIcon", new Dimension(1280, 720));

    private final String rootWindowName;
    private final String iconName;
    private final Dimension rootWindowSize;

    AppType(String rootWindowName, String iconName, Dimension rootWindowSize) {
        this.rootWindowName = rootWindowName;
        this.iconName = iconName;
        this.rootWindowSize = rootWindowSize;
    }

    public String getRootWindowTitle() {
        return String.format("%s - Screen Sharing", rootWindowName);
    }

    public Optional<Image> getIconPath(Class<?> frameClazz) {
        return FileUtils.getRootWindowIconFromResources(frameClazz, this);
    }

    public String getIconName() {
        return iconName;
    }

    public Dimension getRootWindowSize() {
        return rootWindowSize;
    }
}
