package pl.polsl.screensharing.client.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.polsl.screensharing.lib.state.ColoredLabelState;

import java.awt.*;

@Getter
@RequiredArgsConstructor
public enum ConnectionState implements ColoredLabelState {
    CONNECTED("Connected", Color.BLUE),
    CONNECTING("Connecting", Color.ORANGE),
    DISCONNECTED("Disconnected", Color.GRAY),
    ;

    private final String state;
    private final Color color;
}
