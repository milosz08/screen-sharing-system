/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.file;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.icon.AppIcon;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.StringJoiner;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {
    public static Optional<URL> getAssetFileFromResources(Class<?> invokingClazz, String resourcePath, Object... args) {
        final URL iconUrl = invokingClazz.getResource(String.format("/assets/%s", String.format(resourcePath, args)));
        if (iconUrl != null) {
            return Optional.of(iconUrl);
        }
        return Optional.empty();
    }

    public static Optional<ImageIcon> getImageIconFromResources(Class<?> invokingClazz, AppIcon libIcon) {
        final Optional<URL> iconUrl = getAssetFileFromResources(invokingClazz, "icons/%s.png", libIcon.getName());
        return iconUrl.map(ImageIcon::new);
    }

    public static Optional<Image> getRootWindowIconFromResources(Class<?> invokingClazz, AppType appType) {
        final Optional<URL> iconUrl = getAssetFileFromResources(invokingClazz, "%s.png", appType.getIconName());
        return iconUrl.map(url -> new ImageIcon(url).getImage());
    }

    public static String loadAndWrapAsHtmlContent(String assetName, Class<?> invokingClazz, Alignment alignment) {
        final URL descriptionUrl = FileUtils.getAssetFileFromResources(invokingClazz, assetName)
            .orElseThrow(RuntimeException::new);
        try (final InputStream inputStream = descriptionUrl.openStream()) {
            if (inputStream == null) {
                throw new RuntimeException();
            }
            final String rawBuffer = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            final StringJoiner joiner = new StringJoiner(StringUtils.EMPTY)
                .add(String.format("<html><div style='%s;'>", alignment.getHtml()))
                .add(rawBuffer)
                .add("</div></html>");
            log.info("Successfully load {} file from asset resources directory.", assetName);
            return joiner.toString()
                .replaceAll("\n", "<br/>");
        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }
}
