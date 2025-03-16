package pl.polsl.screensharing.lib.net;

public enum SocketState {
    WAITING,
    EXHANGE_KEYS_REQ,
    EXHANGE_KEYS_RES,
    CHECK_PASSWORD_REQ,
    CHECK_PASSWORD_RES,
    SEND_CLIENT_DATA_REQ,
    SEND_CLIENT_DATA_RES,
    EVENT_START_STREAMING,
    EVENT_STOP_STREAMING,
    EVENT_TOGGLE_SCREEN_VISIBILITY,
    KICK_FROM_SESSION,
    END_UP_SESSION,
    ;

    private static final char SEPARATOR = '%';

    public String generateBody(String content) {
        return name() + SEPARATOR + content;
    }

    public static SocketState extractHeader(String merged) {
        return valueOf(merged.substring(0, merged.indexOf(SEPARATOR)));
    }

    public static String extractContent(String merged) {
        return merged.substring(merged.indexOf(SEPARATOR) + 1);
    }
}
