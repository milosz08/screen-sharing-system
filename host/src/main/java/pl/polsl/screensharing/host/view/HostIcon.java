/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.polsl.screensharing.lib.gui.icon.AppIcon;

@Getter
@RequiredArgsConstructor
public enum HostIcon implements AppIcon {
    ADD_LINK("AddLink"),
    APPLICATION_ERROR("ApplicationError"),
    DEBUG_INTERACTIVE_WINDOW("DebugInteractiveWindow"),
    REMOVE_LINK("RemoveLink"),
    SERVER_SETTINGS("ServerSettings"),
    ;

    private final String name;
}
