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
    private final BehaviorSubject<Integer> realFpsBuffer$;
    private final BehaviorSubject<Long> sendBytesPerSec$;
    private final BehaviorSubject<GraphicsDevice> selectedGraphicsDevice$;
    private final BehaviorSubject<QualityLevel> streamingQuality$;
    private final BehaviorSubject<Color> frameColor$;
    private final BehaviorSubject<Boolean> isScreenShowForParticipants$;
    private final BehaviorSubject<Boolean> isShowingFrameSelector$;
    private final BehaviorSubject<Boolean> isCursorShowing$;
    private final BehaviorSubject<CaptureMode> captureMode$;

    public HostState() {
        streamingState$ = BehaviorSubject.createDefault(StreamingState.STOPPED);
        streamingTime$ = BehaviorSubject.createDefault(0L);
        sessionState$ = BehaviorSubject.createDefault(SessionState.INACTIVE);
        sessionTime$ = BehaviorSubject.createDefault(0L);
        realFpsBuffer$ = BehaviorSubject.createDefault(30);
        sendBytesPerSec$ = BehaviorSubject.createDefault(0L);
        selectedGraphicsDevice$ = BehaviorSubject.create();
        streamingQuality$ = BehaviorSubject.createDefault(QualityLevel.GOOD);
        frameColor$ = BehaviorSubject.createDefault(Color.RED);
        isScreenShowForParticipants$ = BehaviorSubject.createDefault(true);
        isShowingFrameSelector$ = BehaviorSubject.createDefault(false);
        isCursorShowing$ = BehaviorSubject.createDefault(true);
        captureMode$ = BehaviorSubject.createDefault(CaptureMode.FULL_FRAME);
    }

    public void updateStreamingState(StreamingState streamingState) {
        streamingState$.onNext(streamingState);
    }

    public void updateStreamingTime(long seconds) {
        streamingTime$.onNext(seconds);
    }

    public void updateSessionState(SessionState sessionState) {
        sessionState$.onNext(sessionState);
    }

    public void updateSessionTime(long seconds) {
        sessionTime$.onNext(seconds);
    }

    public void updateRealFpsBuffer(Integer fps) {
        realFpsBuffer$.onNext(fps);
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

    public Observable<Integer> getRealFpsBuffer$() {
        return realFpsBuffer$.hide();
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

    public CaptureMode getLastEmittedCapturedMode() {
        return captureMode$.getValue();
    }
}
