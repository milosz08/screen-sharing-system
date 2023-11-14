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
    CLOAK_OR_HIDE("CloakOrHide"),
    REMOVE_LINK("RemoveLink"),
    SERVER_SETTINGS("ServerSettings"),
    VISIBLE("Visible"),
    ;

    private final String name;
}
