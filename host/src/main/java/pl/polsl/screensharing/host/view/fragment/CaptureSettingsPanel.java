/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.fragment;

import io.reactivex.rxjava3.core.Observable;
import lombok.Getter;
import pl.polsl.screensharing.host.aggregator.CapturedModeFrameAggregator;
import pl.polsl.screensharing.host.controller.CaptureSettingsController;
import pl.polsl.screensharing.host.state.CaptureMode;
import pl.polsl.screensharing.host.view.HostIcon;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.gui.component.JAppIconButton;

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

    private final JPanel selectFrameVisibilityPanel;
    private final JAppIconButton showSelectFrameButton;
    private final JAppIconButton hideSelectFrameButton;

    private final JButton selectFrameColorButton;
    private final JPanel selectedFrameColorPanel;
    private final JLabel selectedFrameColorLabel;
    private final FrameColorRectInfo frameColorRectInfo;

    private final JCheckBox showCursorCheckbox;

    protected CaptureSettingsPanel(HostWindow hostWindow) {
        super(hostWindow, "Capture settings");
        this.controller = new CaptureSettingsController(hostWindow, this);

        selectedScreenLabel = new JLabel("Selected screen");
        selectedScreenComboBox = new JComboBox<>(new DefaultComboBoxModel<>(controller.getAllGraphicsDevices()));
        selectedScreenComboBox.setRenderer(new GraphicsDeviceCellRenderer());

        captureModeContainer = new JPanel();
        captureModeContainer.setLayout(new BoxLayout(captureModeContainer, BoxLayout.Y_AXIS));
        captureModeButtonGroup = new ButtonGroup();
        fullScreenCaptureModeRadio = new JRadioButton("Capture full screen", true);
        areaCaptureModeRadio = new JRadioButton("Capture selected area");

        selectFrameVisibilityPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        showSelectFrameButton = new JAppIconButton("Show frame", HostIcon.VISIBLE, false, false);
        hideSelectFrameButton = new JAppIconButton("Hide frame", HostIcon.CLOAK_OR_HIDE, false, false);

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
        showSelectFrameButton.addActionListener(e -> controller.toggleShowingFrame(true));
        hideSelectFrameButton.addActionListener(e -> controller.toggleShowingFrame(false));
        selectFrameColorButton.addActionListener(e -> controller.updateFrameColorPicker());
        showCursorCheckbox.addActionListener(e -> controller.toggleShowingCursor());

        captureModeButtonGroup.add(fullScreenCaptureModeRadio);
        captureModeButtonGroup.add(areaCaptureModeRadio);
        captureModeContainer.add(fullScreenCaptureModeRadio);
        captureModeContainer.add(areaCaptureModeRadio);

        selectFrameVisibilityPanel.add(showSelectFrameButton);
        selectFrameVisibilityPanel.add(hideSelectFrameButton);

        selectedFrameColorPanel.add(selectedFrameColorLabel);
        selectedFrameColorPanel.add(frameColorRectInfo);

        drawToGridbag(selectedScreenLabel);
        drawToGridbag(selectedScreenComboBox);
        drawToGridbag(captureModeContainer);
        drawToGridbag(selectFrameVisibilityPanel);
        drawToGridbag(selectFrameColorButton);
        drawToGridbag(selectedFrameColorPanel);
        drawToGridbag(showCursorCheckbox);

        add(mainPanel);
    }

    private void initObservables() {
        hostState.wrapAsDisposable(hostState.getSelectedGraphicsDevice$(), selectedScreenComboBox::setSelectedItem);
        hostState.wrapAsDisposable(hostState.isCursorShowing$(), showCursorCheckbox::setSelected);

        final Observable<CapturedModeFrameAggregator> aggregator = Observable.combineLatest(
            hostState.getCaptureMode$(),
            hostState.isFrameSelectorShowing$(),
            CapturedModeFrameAggregator::new);

        hostState.wrapAsDisposable(aggregator, state -> {
            final CaptureMode captureMode = state.getCaptureMode();
            fullScreenCaptureModeRadio.setSelected(captureMode.equals(CaptureMode.FULL_FRAME));
            areaCaptureModeRadio.setSelected(captureMode.equals(CaptureMode.AREA));

            showSelectFrameButton.setEnabled(!state.isShowingFrame() && captureMode.equals(CaptureMode.AREA));
            hideSelectFrameButton.setEnabled(state.isShowingFrame() && captureMode.equals(CaptureMode.AREA));
        });
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
