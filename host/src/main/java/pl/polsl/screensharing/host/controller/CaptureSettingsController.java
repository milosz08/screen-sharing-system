/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.state.CaptureMode;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.fragment.CaptureSettingsPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CaptureSettingsController {
    private final HostWindow hostWindow;
    private final CaptureSettingsPanel captureSettingsPanel;

    private final List<GraphicsDevice> userGraphicsDevices;

    public CaptureSettingsController(HostWindow hostWindow, CaptureSettingsPanel captureSettingsPanel) {
        this.hostWindow = hostWindow;
        this.captureSettingsPanel = captureSettingsPanel;
        this.userGraphicsDevices = new ArrayList<>();
    }

    public GraphicsDevice[] getAllGraphicsDevices() {
        final HostState hostState = hostWindow.getHostState();

        final GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice[] graphicsDevices = environment.getScreenDevices();

        userGraphicsDevices.addAll(Arrays.asList(graphicsDevices));
        log.info("Detect {} graphics devices: {}", graphicsDevices.length, graphicsDevices);

        hostState.updateSelectedGraphicsDevice(graphicsDevices[0]);
        log.info("Set graphics device {} as primary", graphicsDevices[0]);

        return userGraphicsDevices.toArray(new GraphicsDevice[0]);
    }

    public void updateSelectedDisplayDevice() {
        final HostState hostState = hostWindow.getHostState();
        final GraphicsDevice graphicsDevice = (GraphicsDevice) captureSettingsPanel
            .getSelectedScreenComboBox()
            .getSelectedItem();
        if (graphicsDevice == null) {
            return;
        }
        hostState.updateSelectedGraphicsDevice(graphicsDevice);
        log.info("Updated primary graphics device to {}", graphicsDevice);
    }

    public void toggleScreenCaptureMode() {
        final HostState hostState = hostWindow.getHostState();
        final boolean isArea = captureSettingsPanel.getAreaCaptureModeRadio().isSelected();
        CaptureMode captureMode = CaptureMode.FULL_FRAME;
        if (isArea) {
            captureMode = CaptureMode.AREA;
        }
        hostState.updateShowingFrameSelectorState(isArea);
        hostState.updateCaptureMode(captureMode);
        log.info("Updated screen capture mode to {}", captureMode);
    }

    public void toggleShowingFrame(boolean isShowing) {
        final HostState hostState = hostWindow.getHostState();
        hostState.updateShowingFrameSelectorState(isShowing);
        log.info("Helper resizable frame is {}", isShowing ? "on" : "off");
    }

    public void updateFrameColorPicker() {
        final HostState hostState = hostWindow.getHostState();
        final Color selectedColor = JColorChooser.showDialog(captureSettingsPanel,
            "Choose a frame color", hostState.getLastEmittedFrameColor());
        if (selectedColor == null) {
            return;
        }
        hostState.updateFrameColor(selectedColor);
        log.info("Updated resizable frame color to {}", selectedColor);
    }

    public void toggleShowingCursor() {
        final HostState hostState = hostWindow.getHostState();
        final boolean isShowing = captureSettingsPanel
            .getShowCursorCheckbox()
            .isSelected();
        hostState.updateShowingCursorState(isShowing);
        log.info("Showing cursor on screen state is {}", isShowing ? "on" : "off");
    }
}
