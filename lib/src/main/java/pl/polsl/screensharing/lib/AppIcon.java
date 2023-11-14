/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppIcon {
    ADD_CONNECTION("AddConnection"),
    CANCEL("Cancel"),
    CHECK_BOX_LIST("CheckBoxList"),
    CODE_INFORMATION("CodeInformation"),
    CODE_INFORMATION_RULE("CodeInformationRule"),
    CONNECT_TO_REMOTE_SERVER("ConnectToRemoteServer"),
    DELETE_CLAUSE("DeleteClause"),
    DELETE_TABLE("DeleteTable"),
    DISCONNECT("Disconnect"),
    SAVE("Save"),
    SERVER_SETTINGS("ServerSettings");

    private final String name;
}
