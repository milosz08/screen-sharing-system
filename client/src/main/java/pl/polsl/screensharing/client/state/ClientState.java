/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.state;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.Getter;
import pl.polsl.screensharing.client.model.FastConnectionDetails;
import pl.polsl.screensharing.client.model.SavedConnection;
import pl.polsl.screensharing.lib.state.AbstractDisposableProvider;

import java.util.SortedSet;
import java.util.TreeSet;

public class ClientState extends AbstractDisposableProvider {
    private final BehaviorSubject<ConnectionState> connectionState$;
    private final BehaviorSubject<Long> connectionTime$;
    private final BehaviorSubject<RecordingState> recordingState$;
    private final BehaviorSubject<Long> recordingTime$;
    private final BehaviorSubject<Long> recvBytesPerSec$;
    private final BehaviorSubject<FastConnectionDetails> fastConnectionDetails$;
    private final BehaviorSubject<SortedSet<SavedConnection>> savedConnections$;

    @Getter
    private final PersistedStateLoader persistedStateLoader;

    public ClientState() {
        this.persistedStateLoader = new PersistedStateLoader(this);
        this.persistedStateLoader.initPersistor(new PersistedState());

        this.connectionState$ = BehaviorSubject.createDefault(ConnectionState.DISCONNECTED);
        this.connectionTime$ = BehaviorSubject.createDefault(0L);
        this.recordingState$ = BehaviorSubject.createDefault(RecordingState.IDLE);
        this.recordingTime$ = BehaviorSubject.createDefault(0L);
        this.recvBytesPerSec$ = BehaviorSubject.createDefault(0L);
        this.fastConnectionDetails$ = BehaviorSubject.createDefault(new FastConnectionDetails());
        this.savedConnections$ = BehaviorSubject.createDefault(new TreeSet<>());

        this.persistedStateLoader.loadApplicationSavedState();
    }

    public void updateConnectionState(ConnectionState connectionState) {
        connectionState$.onNext(connectionState);
    }

    public void updateConnectionTime(Long seconds) {
        connectionTime$.onNext(seconds);
    }

    public void updateRecordingState(RecordingState recordingState) {
        recordingState$.onNext(recordingState);
    }

    public void updateRecordingTime(Long seconds) {
        recordingTime$.onNext(seconds);
    }

    public void updateRecvBytesPerSec(Long bytesPerSec) {
        recvBytesPerSec$.onNext(bytesPerSec);
    }

    public void updateFastConnectionDetails(FastConnectionDetails fastConnectionDetails) {
        fastConnectionDetails$.onNext(fastConnectionDetails);
    }

    public void updateSavedConnections(SortedSet<SavedConnection> savedConnectionSet) {
        savedConnections$.onNext(savedConnectionSet);
    }

    public Observable<ConnectionState> getConnectionState$() {
        return connectionState$.hide();
    }

    public Observable<Long> getConnectionTime$() {
        return connectionTime$.hide();
    }

    public Observable<RecordingState> getRecordingState$() {
        return recordingState$.hide();
    }

    public Observable<Long> getRecordingTime$() {
        return recordingTime$.hide();
    }

    public Observable<Long> getRecvBytesPerSec$() {
        return recvBytesPerSec$.hide();
    }

    public Observable<FastConnectionDetails> getFastConnectionDetails$() {
        return fastConnectionDetails$.hide();
    }

    public Observable<SortedSet<SavedConnection>> getSavedConnections$() {
        return savedConnections$.hide();
    }

    public SortedSet<SavedConnection> getLastEmittedSavedConnections() {
        return savedConnections$.getValue();
    }

    public FastConnectionDetails getLastEmittedFastConnectionDetails() {
        return fastConnectionDetails$.getValue();
    }
}
