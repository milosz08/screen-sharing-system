/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.state;

import lombok.Getter;
import pl.polsl.screensharing.client.dto.ConnectionDetailsDto;
import pl.polsl.screensharing.client.dto.LastConnectionRowDto;

import java.util.ArrayList;
import java.util.List;

@Getter
class SavedStateDto {
    private final ConnectionDetailsDto connectionDetails;
    private final List<LastConnectionRowDto> lastConnections;

    SavedStateDto() {
        this.connectionDetails = new ConnectionDetailsDto();
        this.lastConnections = new ArrayList<>();
    }
}
