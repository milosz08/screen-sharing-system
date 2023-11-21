/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.QualityLevel;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.fragment.GraphicsSettingsPanel;

@Slf4j
@RequiredArgsConstructor
public class GraphicsSettingsController {
    private final HostWindow hostWindow;
    private final GraphicsSettingsPanel graphicsSettingsPanel;

    public void updateStreamingQuality() {
        final HostState hostState = hostWindow.getHostState();
        final QualityLevel qualityLevel = (QualityLevel) graphicsSettingsPanel
            .getQualityLevelComboBox()
            .getSelectedItem();
        if (qualityLevel == null) {
            return;
        }
        hostState.updateStreamingQuality(qualityLevel);
        log.info("Updated streaming quality level to {}", qualityLevel);
    }
}
