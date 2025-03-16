package pl.polsl.screensharing.host.view;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.polsl.screensharing.lib.icon.AppIcon;

@Getter
@RequiredArgsConstructor
public enum HostIcon implements AppIcon {
    ADD_LINK("AddLink"),
    APPLICATION_ERROR("ApplicationError"),
    CLOAK_OR_HIDE("CloakOrHide"),
    CURSOR("Cursor"),
    IN_USER_BY_OTHER_USER("InUseByOtherUser"),
    DEBUG_INTERACTIVE_WINDOW("DebugInteractiveWindow"),
    LOOKUP_GROUP_MEMBERS("LookupGroupMembers"),
    OPEN_QUERY("OpenQuery"),
    REMOVE_LINK("RemoveLink"),
    STOP_QUERY("StopQuery"),
    TEAM_PROJECT_COLLECTION_OFFLINE("TeamProjectCollectionOffline"),
    VISIBLE("Visible"),
    ;

    private final String name;
}
