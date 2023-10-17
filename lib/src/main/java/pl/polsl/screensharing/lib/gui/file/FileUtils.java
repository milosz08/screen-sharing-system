/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.file;

import pl.polsl.screensharing.lib.AppIcon;
import pl.polsl.screensharing.lib.AppType;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Optional;

public class FileUtils {
    private FileUtils() {
    }

    public static Optional<URL> getAssetFileFromResources(Class<?> invokingClazz, String resourcePath, Object... args) {
        final URL iconUrl = invokingClazz.getResource(String.format("/assets/%s", String.format(resourcePath, args)));
        if (iconUrl != null) {
            return Optional.of(iconUrl);
        }
        return Optional.empty();
    }

    public static Optional<ImageIcon> getImageIconFromResources(Class<?> invokingClazz, AppIcon appIcon) {
        final Optional<URL> iconUrl = getAssetFileFromResources(invokingClazz, "icons/%s.png", appIcon.getName());
        return iconUrl.map(ImageIcon::new);
    }

    public static Optional<Image> getRootWindowIconFromResources(Class<?> invokingClazz, AppType appType) {
        final Optional<URL> iconUrl = getAssetFileFromResources(invokingClazz, "%s.png", appType.getIconName());
        return iconUrl.map(url -> new ImageIcon(url).getImage());
    }
}
