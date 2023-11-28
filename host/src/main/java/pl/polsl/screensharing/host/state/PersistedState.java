/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.state;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import pl.polsl.screensharing.host.model.FrameColorRgb;
import pl.polsl.screensharing.host.model.SessionDetails;

import java.awt.*;
import java.util.Base64;

@Getter
@Setter
public class PersistedState {
    @JsonProperty("sessionDetails")
    private final SessionDetails sessionDetails;

    @JsonProperty("frameColor")
    private FrameColorRgb frameColor;

    @JsonProperty("isCursorShowing")
    private Boolean isCursorShowing;

    public PersistedState() {
        sessionDetails = new SessionDetails();
        frameColor = new FrameColorRgb(Color.RED);
        isCursorShowing = true;
    }

    @JsonIgnore
    public SessionDetails getSessionDetailsWithDecryptedPassword() {
        sessionDetails.setPassword(new String(Base64.getDecoder().decode(sessionDetails.getPassword())));
        return sessionDetails;
    }
}
