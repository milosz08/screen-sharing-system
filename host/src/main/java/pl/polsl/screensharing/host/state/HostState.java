/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.state;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import pl.polsl.screensharing.lib.state.AbstractDisposableProvider;

public class HostState extends AbstractDisposableProvider {
    private final BehaviorSubject<StreamingState> streamingState$;
    private final BehaviorSubject<Long> streamingTime$;
    private final BehaviorSubject<SessionState> sessionState$;
    private final BehaviorSubject<Long> sessionTime$;
    private final BehaviorSubject<Integer> streamingFps$;
    private final BehaviorSubject<Long> sendBytesPerSec$;

    public HostState() {
        this.streamingState$ = BehaviorSubject.createDefault(StreamingState.STOPPED);
        this.streamingTime$ = BehaviorSubject.createDefault(0L);
        this.sessionState$ = BehaviorSubject.createDefault(SessionState.INACTIVE);
        this.sessionTime$ = BehaviorSubject.createDefault(0L);
        this.streamingFps$ = BehaviorSubject.createDefault(15);
        this.sendBytesPerSec$ = BehaviorSubject.createDefault(0L);
    }

    public void updateStreamingState(StreamingState streamingState) {
        streamingState$.onNext(streamingState);
    }

    public void updateStreamingTime(Long seconds) {
        streamingTime$.onNext(seconds);
    }

    public void updateSessionState(SessionState sessionState) {
        sessionState$.onNext(sessionState);
    }

    public void updateSessionTime(Long seconds) {
        sessionTime$.onNext(seconds);
    }

    public void updateStreamingFps(Integer fps) {
        streamingFps$.onNext(fps);
    }

    public void updateSendBytesPerSec(Long bytesPerSec) {
        sendBytesPerSec$.onNext(bytesPerSec);
    }

    public Observable<StreamingState> getStreamingState$() {
        return streamingState$.hide();
    }

    public Observable<Long> getStreamingTime$() {
        return streamingTime$.hide();
    }

    public Observable<SessionState> getSessionState$() {
        return sessionState$.hide();
    }

    public Observable<Long> getSessionTime$() {
        return sessionTime$.hide();
    }

    public Observable<Integer> getStreamingFps$() {
        return streamingFps$.hide();
    }

    public Observable<Long> getSendBytesPerSec$() {
        return sendBytesPerSec$.hide();
    }
}
