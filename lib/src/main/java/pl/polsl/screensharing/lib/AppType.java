/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib;

import pl.polsl.screensharing.lib.gui.file.FileUtils;

import java.awt.*;
import java.util.Optional;

public enum AppType {
    HOST("HOST", "HostIcon", "host.json", new Dimension(1280, 720)),
    CLIENT("CLIENT", "ClientIcon", "client.json", new Dimension(1280, 720));

    private final String rootWindowName;
    private final String iconName;
    private final String configFileName;
    private final Dimension rootWindowSize;

    AppType(String rootWindowName, String iconName, String configFileName, Dimension rootWindowSize) {
        this.rootWindowName = rootWindowName;
        this.iconName = iconName;
        this.configFileName = configFileName;
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

    public String getConfigFileName() {
        return configFileName;
    }

    public Dimension getRootWindowSize() {
        return rootWindowSize;
    }
}
