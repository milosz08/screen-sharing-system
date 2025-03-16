package pl.polsl.screensharing.lib;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.polsl.screensharing.lib.file.FileUtils;

import java.awt.*;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum AppType {
    HOST("HOST", "HostIcon", "host.json", new Dimension(1280, 720)),
    CLIENT("CLIENT", "ClientIcon", "client.json", new Dimension(1280, 720)),
    ;

    private final String rootWindowName;
    private final String iconName;
    private final String configFileName;
    private final Dimension rootWindowSize;

    public String getRootWindowTitle() {
        return String.format("%s - Screen Sharing", rootWindowName);
    }

    public Optional<Image> getIconPath(Class<?> frameClazz) {
        return FileUtils.getRootWindowIconFromResources(frameClazz, this);
    }
}
