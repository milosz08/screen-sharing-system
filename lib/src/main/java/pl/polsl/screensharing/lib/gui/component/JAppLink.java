/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.polsl.screensharing.lib.gui.MouseClickEvent;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class JAppLink extends JLabel {
    private static final Logger LOG = LoggerFactory.getLogger(JAppLink.class);

    private final URI uri;

    public JAppLink(String link, String placeholder) {
        this.uri = generateUri(link);
        setText(String.format("<html><a href='%s'>%s</a></html>", link, placeholder));
        setForeground(Color.BLUE);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseClickEvent(this::openUri));
    }

    private URI generateUri(String link) {
        try {
            return new URI(link);
        } catch (URISyntaxException ex) {
            LOG.error("Unable to create URI. Link is broken.");
        }
        return null;
    }

    private void openUri() {
        try {
            if (!Desktop.isDesktopSupported()) {
                return;
            }
            Desktop.getDesktop().browse(uri);
        } catch (IOException ex) {
            LOG.error("Unable to open link. Desktop not supported");
        }
    }
}
