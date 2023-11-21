/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.fragment;

import lombok.Getter;
import pl.polsl.screensharing.host.controller.CaptureSettingsController;
import pl.polsl.screensharing.host.state.CaptureMode;
import pl.polsl.screensharing.host.state.StreamingState;
import pl.polsl.screensharing.host.view.HostWindow;

import javax.swing.*;
import java.awt.*;

@Getter
public class CaptureSettingsPanel extends AbstractScreenCaptureRightPanel {
    private final CaptureSettingsController controller;

    private final JLabel selectedScreenLabel;
    private final JComboBox<GraphicsDevice> selectedScreenComboBox;

    private final JPanel captureModeContainer;
    private final ButtonGroup captureModeButtonGroup;
    private final JRadioButton fullScreenCaptureModeRadio;
    private final JRadioButton areaCaptureModeRadio;

    private final JButton selectFrameColorButton;
    private final JPanel selectedFrameColorPanel;
    private final JLabel selectedFrameColorLabel;
    private final FrameColorRectInfo frameColorRectInfo;

    private final JCheckBox showCursorCheckbox;

    protected CaptureSettingsPanel(HostWindow hostWindow) {
        super(hostWindow, "Capture settings");
        controller = new CaptureSettingsController(hostWindow, this);

        selectedScreenLabel = new JLabel("Selected screen");
        selectedScreenComboBox = new JComboBox<>(new DefaultComboBoxModel<>(controller.getAllGraphicsDevices()));
        selectedScreenComboBox.setRenderer(new GraphicsDeviceCellRenderer());

        captureModeContainer = new JPanel();
        captureModeContainer.setLayout(new BoxLayout(captureModeContainer, BoxLayout.Y_AXIS));
        captureModeButtonGroup = new ButtonGroup();
        fullScreenCaptureModeRadio = new JRadioButton("Capture full screen", true);
        areaCaptureModeRadio = new JRadioButton("Capture selected area");

        selectFrameColorButton = new JButton("Select frame color");
        selectedFrameColorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        selectedFrameColorLabel = new JLabel("Frame color:");
        selectedFrameColorLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        frameColorRectInfo = new FrameColorRectInfo(hostState);

        showCursorCheckbox = new JCheckBox("Show cursor", true);

        initObservables();

        selectedScreenComboBox.addActionListener(e -> controller.updateSelectedDisplayDevice());
        fullScreenCaptureModeRadio.addActionListener(e -> controller.toggleScreenCaptureMode());
        areaCaptureModeRadio.addActionListener(e -> controller.toggleScreenCaptureMode());
        selectFrameColorButton.addActionListener(e -> controller.updateFrameColorPicker());
        showCursorCheckbox.addActionListener(e -> controller.toggleShowingCursor());

        captureModeButtonGroup.add(fullScreenCaptureModeRadio);
        captureModeButtonGroup.add(areaCaptureModeRadio);
        captureModeContainer.add(fullScreenCaptureModeRadio);
        captureModeContainer.add(areaCaptureModeRadio);

        selectedFrameColorPanel.add(selectedFrameColorLabel);
        selectedFrameColorPanel.add(frameColorRectInfo);

        drawToGridbag(selectedScreenLabel);
        drawToGridbag(selectedScreenComboBox);
        drawToGridbag(captureModeContainer);
        drawToGridbag(selectFrameColorButton);
        drawToGridbag(selectedFrameColorPanel);
        drawToGridbag(showCursorCheckbox);

        add(mainPanel);
    }

    private void initObservables() {
        hostState.wrapAsDisposable(hostState.getCaptureMode$(), captureMode -> {
            fullScreenCaptureModeRadio.setSelected(captureMode.equals(CaptureMode.FULL_FRAME));
            areaCaptureModeRadio.setSelected(captureMode.equals(CaptureMode.AREA));
        });
        hostState.wrapAsDisposable(hostState.getStreamingState$(), streamingState -> {
            final boolean isStreaming = streamingState.equals(StreamingState.STREAMING);
            fullScreenCaptureModeRadio.setEnabled(!isStreaming);
            areaCaptureModeRadio.setEnabled(!isStreaming);
        });
        hostState.wrapAsDisposable(hostState.getSelectedGraphicsDevice$(), selectedScreenComboBox::setSelectedItem);
        hostState.wrapAsDisposable(hostState.isCursorShowing$(), showCursorCheckbox::setSelected);
    }

    private static class GraphicsDeviceCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus
        ) {
            if (value instanceof GraphicsDevice) {
                final GraphicsDevice graphicsDevice = (GraphicsDevice) value;
                value = graphicsDevice.getIDstring().replace("\\", "");
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
}
