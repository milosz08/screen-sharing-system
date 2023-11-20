/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.fragment;

import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostWindow;

import javax.swing.*;
import java.awt.*;

public class DisableScreenPanel extends JPanel {
    private final HostState hostState;

    private final JLabel textLabel;

    public DisableScreenPanel(HostWindow hostWindow) {
        hostState = hostWindow.getHostState();

        setLayout(new BorderLayout());
        setVisible(false);
        setBackground(Color.GRAY);

        textLabel = new JLabel("Showing screen is disabled");
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font(new JLabel().getFont().getFontName(), Font.PLAIN, 15));

        initObservables();

        add(textLabel);
    }

    private void initObservables() {
        hostState.wrapAsDisposable(hostState.isScreenIsShowForParticipants$(), isShowing -> {
            setVisible(!isShowing);
        });
    }
}
