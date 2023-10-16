/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Optional;

public enum AppType {
    HOST("HOST", "host-icon", new Dimension(1280, 720)),
    CLIENT("CLIENT", "client-icon", new Dimension(1280, 720));

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
        final URL iconUrl = frameClazz.getClassLoader()
            .getResource(String.format("/assets/%s.png", iconName));
        if (iconUrl == null) {
            return Optional.empty();
        }
        return Optional.of(new ImageIcon(iconUrl).getImage());
    }

    public Dimension getRootWindowSize() {
        return rootWindowSize;
    }
}
