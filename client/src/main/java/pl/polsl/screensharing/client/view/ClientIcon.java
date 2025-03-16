package pl.polsl.screensharing.client.view;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.polsl.screensharing.lib.icon.AppIcon;

@Getter
@RequiredArgsConstructor
public enum ClientIcon implements AppIcon {
    ADD_CONNECTION("AddConnection"),
    CONNECT_TO_REMOTE_SERVER("ConnectToRemoteServer"),
    DISCONNECT("Disconnect"),
    TAKE_SNAPSHOT("TakeSnapshot"),
    ;

    private final String name;
}
