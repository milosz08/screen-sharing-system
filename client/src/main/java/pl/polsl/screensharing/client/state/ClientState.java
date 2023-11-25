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
import pl.polsl.screensharing.lib.SharedConstants;
import pl.polsl.screensharing.lib.state.AbstractDisposableProvider;

import java.util.SortedSet;
import java.util.TreeSet;

public class ClientState extends AbstractDisposableProvider {
    private final BehaviorSubject<ConnectionState> connectionState$;
    private final BehaviorSubject<Long> connectionTime$;
    private final BehaviorSubject<Long> recvBytesPerSec$;
    private final BehaviorSubject<FastConnectionDetails> fastConnectionDetails$;
    private final BehaviorSubject<SortedSet<SavedConnection>> savedConnections$;
    private final BehaviorSubject<VisibilityState> visibilityState$;
    private final BehaviorSubject<Integer> lostFramesCount$;
    private final BehaviorSubject<Double> frameAspectRatio$;

    @Getter
    private final PersistedStateLoader persistedStateLoader;

    public ClientState() {
        persistedStateLoader = new PersistedStateLoader(this);
        persistedStateLoader.initPersistor(new PersistedState());

        connectionState$ = BehaviorSubject.createDefault(ConnectionState.DISCONNECTED);
        connectionTime$ = BehaviorSubject.createDefault(0L);
        recvBytesPerSec$ = BehaviorSubject.createDefault(0L);
        fastConnectionDetails$ = BehaviorSubject.createDefault(new FastConnectionDetails());
        savedConnections$ = BehaviorSubject.createDefault(new TreeSet<>());
        visibilityState$ = BehaviorSubject.createDefault(VisibilityState.WAITING_FOR_CONNECTION);
        lostFramesCount$ = BehaviorSubject.createDefault(0);
        frameAspectRatio$ = BehaviorSubject.createDefault(SharedConstants.DEFAULT_ASPECT_RATIO);

        persistedStateLoader.loadApplicationSavedState();
    }

    public void updateConnectionState(ConnectionState connectionState) {
        connectionState$.onNext(connectionState);
    }

    public void updateConnectionTime(Long seconds) {
        connectionTime$.onNext(seconds);
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

    public void updateVisibilityState(VisibilityState visibilityState) {
        visibilityState$.onNext(visibilityState);
    }

    public void updateLostFramesCount(int lostFramesCount) {
        lostFramesCount$.onNext(lostFramesCount);
    }

    public void updateFrameAspectRation(double frameAspectRatio) {
        frameAspectRatio$.onNext(frameAspectRatio);
    }

    public Observable<ConnectionState> getConnectionState$() {
        return connectionState$.hide();
    }

    public Observable<Long> getConnectionTime$() {
        return connectionTime$.hide();
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

    public Observable<VisibilityState> getVisibilityState$() {
        return visibilityState$.hide();
    }

    public Observable<Integer> getLostFramesCount$() {
        return lostFramesCount$.hide();
    }

    public Observable<Double> getFrameAspectRatio$() {
        return frameAspectRatio$.hide();
    }

    public SortedSet<SavedConnection> getLastEmittedSavedConnections() {
        return savedConnections$.getValue();
    }

    public FastConnectionDetails getLastEmittedFastConnectionDetails() {
        return fastConnectionDetails$.getValue();
    }
}
