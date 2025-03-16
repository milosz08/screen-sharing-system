package pl.polsl.screensharing.lib.file;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Alignment {
    LEFT("text-align: left;"),
    RIGHT("text-align: right;"),
    CENTER("text-align: center;"),
    ;

    private final String html;
}
