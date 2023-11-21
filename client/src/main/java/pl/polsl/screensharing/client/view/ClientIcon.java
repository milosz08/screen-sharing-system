/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.polsl.screensharing.lib.gui.icon.AppIcon;

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
