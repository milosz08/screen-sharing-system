package pl.polsl.screensharing.lib.icon;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LibIcon implements AppIcon {
    CANCEL("Cancel"),
    CHECK_BOX_LIST("CheckBoxList"),
    CODE_INFORMATION_RULE("CodeInformationRule"),
    DELETE("Delete"),
    DELETE_CLAUSE("DeleteClause"),
    DELETE_TABLE("DeleteTable"),
    DOWNLOAD("Download"),
    HELP_TABLE_OF_CONTENTS("HelpTableOfContents"),
    PRINT("Print"),
    SAVE("Save"),
    STATUS_INFORMATION("StatusInformation"),
    UPLOAD("Upload"),
    ;

    private final String name;
}
