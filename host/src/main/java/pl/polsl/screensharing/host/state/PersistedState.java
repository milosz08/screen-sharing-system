/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.state;

import lombok.Getter;
import pl.polsl.screensharing.host.model.SessionDetails;

@Getter
public class PersistedState {
    private final SessionDetails sessionDetails;

    public PersistedState() {
        sessionDetails = new SessionDetails();
    }
}
