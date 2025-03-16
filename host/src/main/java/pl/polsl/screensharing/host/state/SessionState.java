package pl.polsl.screensharing.host.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.polsl.screensharing.lib.state.ColoredLabelState;

import java.awt.*;

@Getter
@RequiredArgsConstructor
public enum SessionState implements ColoredLabelState {
    CREATED("Created", Color.BLUE),
    INACTIVE("Inactive", Color.GRAY),
    ;

    private final String state;
    private final Color color;
}
