/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.state;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import pl.polsl.screensharing.lib.state.AbstractDisposableProvider;

import java.awt.*;

public class HostState extends AbstractDisposableProvider {
    private final BehaviorSubject<StreamingState> streamingState$;
    private final BehaviorSubject<Long> streamingTime$;
    private final BehaviorSubject<SessionState> sessionState$;
    private final BehaviorSubject<Long> sessionTime$;
    private final BehaviorSubject<Integer> streamingFps$;
    private final BehaviorSubject<Long> sendBytesPerSec$;
    private final BehaviorSubject<GraphicsDevice> selectedGraphicsDevice$;
    private final BehaviorSubject<QualityLevel> streamingQuality$;
    private final BehaviorSubject<Color> frameColor$;
    private final BehaviorSubject<Boolean> isScreenShowForParticipants$;
    private final BehaviorSubject<Boolean> isShowingFrameSelector$;
    private final BehaviorSubject<Boolean> isCursorShowing$;
    private final BehaviorSubject<CaptureMode> captureMode$;

    public HostState() {
        this.streamingState$ = BehaviorSubject.createDefault(StreamingState.STOPPED);
        this.streamingTime$ = BehaviorSubject.createDefault(0L);
        this.sessionState$ = BehaviorSubject.createDefault(SessionState.INACTIVE);
        this.sessionTime$ = BehaviorSubject.createDefault(0L);
        this.streamingFps$ = BehaviorSubject.createDefault(30);
        this.sendBytesPerSec$ = BehaviorSubject.createDefault(0L);
        this.selectedGraphicsDevice$ = BehaviorSubject.create();
        this.streamingQuality$ = BehaviorSubject.createDefault(QualityLevel.BEST);
        this.frameColor$ = BehaviorSubject.createDefault(Color.YELLOW);
        this.isScreenShowForParticipants$ = BehaviorSubject.createDefault(true);
        this.isShowingFrameSelector$ = BehaviorSubject.createDefault(false);
        this.isCursorShowing$ = BehaviorSubject.createDefault(true);
        this.captureMode$ = BehaviorSubject.createDefault(CaptureMode.FULL_FRAME);
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

    public void updateSelectedGraphicsDevice(GraphicsDevice graphicsDevice) {
        selectedGraphicsDevice$.onNext(graphicsDevice);
    }

    public void updateStreamingQuality(QualityLevel qualityLevel) {
        streamingQuality$.onNext(qualityLevel);
    }

    public void updateFrameColor(Color color) {
        frameColor$.onNext(color);
    }

    public void updateShowingScreenForParticipants(boolean isShowing) {
        isScreenShowForParticipants$.onNext(isShowing);
    }

    public void updateShowingFrameSelectorState(boolean isShowing) {
        isShowingFrameSelector$.onNext(isShowing);
    }

    public void updateShowingCursorState(boolean isShowing) {
        isCursorShowing$.onNext(isShowing);
    }

    public void updateCaptureMode(CaptureMode captureMode) {
        captureMode$.onNext(captureMode);
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

    public Observable<GraphicsDevice> getSelectedGraphicsDevice$() {
        return selectedGraphicsDevice$.hide();
    }

    public Observable<QualityLevel> getStreamingQualityLevel$() {
        return streamingQuality$.hide();
    }

    public Observable<Color> getFrameColor$() {
        return frameColor$.hide();
    }

    public Observable<Boolean> isScreenIsShowForParticipants$() {
        return isScreenShowForParticipants$.hide();
    }

    public Observable<Boolean> isFrameSelectorShowing$() {
        return isShowingFrameSelector$.hide();
    }

    public Observable<Boolean> isCursorShowing$() {
        return isCursorShowing$.hide();
    }

    public Observable<CaptureMode> getCaptureMode$() {
        return captureMode$.hide();
    }

    public Color getLastEmittedFrameColor() {
        return frameColor$.getValue();
    }
}
