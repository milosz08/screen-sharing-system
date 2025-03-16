package pl.polsl.screensharing.lib.gui.component;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.lib.gui.lambda.MouseClickEvent;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class JAppLink extends JLabel {
    private final URI uri;

    public JAppLink(String link, String placeholder) {
        uri = generateUri(link);
        setText(String.format("<html><a href='%s'>%s</a></html>", link, placeholder));
        setForeground(Color.BLUE);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseClickEvent(this::openUri));
    }

    private URI generateUri(String link) {
        try {
            return new URI(link);
        } catch (URISyntaxException ex) {
            log.error("Unable to create URI. Link is broken.");
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
            log.error("Unable to open link. Desktop not supported");
        }
    }
}
