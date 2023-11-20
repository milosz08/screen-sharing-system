/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.fragment;

import lombok.Getter;
import pl.polsl.screensharing.host.controller.GraphicsSettingsController;
import pl.polsl.screensharing.host.state.QualityLevel;
import pl.polsl.screensharing.host.view.HostWindow;

import javax.swing.*;

@Getter
public class GraphicsSettingsPanel extends AbstractScreenCaptureRightPanel {
    private final GraphicsSettingsController controller;

    private final JLabel selectedQualityLabel;
    private final JComboBox<QualityLevel> qualityLevelComboBox;

    protected GraphicsSettingsPanel(HostWindow hostWindow) {
        super(hostWindow, "Graptics settings");
        controller = new GraphicsSettingsController(hostWindow, this);

        selectedQualityLabel = new JLabel("Select quality");
        qualityLevelComboBox = new JComboBox<>(new DefaultComboBoxModel<>(QualityLevel.values()));

        initObservables();

        qualityLevelComboBox.addActionListener(e -> controller.updateStreamingQuality());

        drawToGridbag(selectedQualityLabel);
        drawToGridbag(qualityLevelComboBox);

        add(mainPanel);
    }

    private void initObservables() {
        hostState.wrapAsDisposable(hostState.getStreamingQualityLevel$(), qualityLevel -> {
            this.getQualityLevelComboBox().setSelectedItem(qualityLevel);
        });
    }
}
