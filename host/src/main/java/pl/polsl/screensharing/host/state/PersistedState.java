/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.state;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import pl.polsl.screensharing.host.model.SessionDetails;

@Getter
public class PersistedState {
    private final SessionDetails sessionDetails;

    public PersistedState() {
        sessionDetails = new SessionDetails();

    @JsonIgnore
    public SessionDetails getSessionDetailsWithDecryptedPassword() {
        sessionDetails.setPassword(new String(Base64.getDecoder().decode(sessionDetails.getPassword())));
        return sessionDetails;
    }
}
