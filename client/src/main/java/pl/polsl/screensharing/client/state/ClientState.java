/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.state;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.client.dto.FastConnDetailsDto;
import pl.polsl.screensharing.client.dto.SavedConnDetailsDto;
import pl.polsl.screensharing.lib.state.IntegrityProvider;

import java.util.Optional;
import java.util.SortedSet;

@Slf4j
@Getter
@Setter
public class ClientState implements IntegrityProvider {
    private boolean isRecording;
    private ConnectState connectionState;
    private long connectionTime;
    private long recordingTime;
    private long recvBytesPerSec;
    private FastConnDetailsDto fastConnDetails;
    private SortedSet<SavedConnDetailsDto> savedConnDetails;
    private final PersistedStateLoader persistedStateLoader;

    public ClientState() {
        this.persistedStateLoader = new PersistedStateLoader(this);
        this.persistedStateLoader.initPersistor(new PersistedState());
        this.persistedStateLoader.loadApplicationSavedState();
        this.connectionState = ConnectState.DISCONNECTED;
    }

    public boolean addNewSavedConn(SavedConnDetailsDto detailsDto) {
        final int index = savedConnDetails.size();
        detailsDto.setId(index);
        final boolean isNew = savedConnDetails.stream()
            .noneMatch(details -> details.equals(detailsDto));
        if (isNew) {
            savedConnDetails.add(detailsDto);
            log.info("Add new saved connection: {}.", detailsDto);
            persistedStateLoader.persistSavedConnDetails();
            return true;
        }
        log.info("Saved connection {} already exist. Skipping.", detailsDto);
        return false;
    }

    public void copyAndPushSavedConnDetails(SortedSet<SavedConnDetailsDto> rowDtos) {
        savedConnDetails.clear();
        savedConnDetails.addAll(rowDtos);
        log.info("Update last connections table rows. Updated table: {}.", rowDtos);
        persistedStateLoader.persistSavedConnDetails();
    }

    public void removeAllSavedConnDetails() {
        savedConnDetails.clear();
        log.info("Removed all saved last connections.");
        persistedStateLoader.persistSavedConnDetails();
    }

    public void removeConnDetailsByIndex(int index) {
        if (index >= 0 && index < savedConnDetails.size()) {
            final Optional<SavedConnDetailsDto> removedOptional = savedConnDetails.stream()
                .filter(details -> details.getId() == index)
                .findFirst();
            if (!removedOptional.isPresent()) {
                return;
            }
            final SavedConnDetailsDto removed = removedOptional.get();
            savedConnDetails.remove(removed);

            int i = 0;
            for (final SavedConnDetailsDto notRemovable : savedConnDetails) {
                notRemovable.setId(i++);
            }
            log.info("Removed last connection: {}.", removed);
            log.info("Reorganized rows: {}.", savedConnDetails);
            persistedStateLoader.persistSavedConnDetails();
        }
    }

    public void persistFastConnDetails(FastConnDetailsDto detailsDto) {
        fastConnDetails.setFastConnDetailsDto(detailsDto);
        log.info("Update fast connection details: {}.", detailsDto);
        persistedStateLoader.persistFastConnDetails();
    }

    public boolean isConnected() {
        return connectionState.equals(ConnectState.CONNECTED);
    }

    public void increaseConnectionTime() {
        connectionTime++;
    }

    public void increaseRecordingTime() {
        recordingTime++;
    }

    @Override
    public boolean isIntegrityStateTrue() {
        return isRecording;
    }
}
