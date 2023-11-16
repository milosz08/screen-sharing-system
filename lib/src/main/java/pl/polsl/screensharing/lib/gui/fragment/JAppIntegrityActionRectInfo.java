/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.fragment;

import lombok.RequiredArgsConstructor;
import pl.polsl.screensharing.lib.state.IntegrityProvider;

import javax.swing.*;
import java.awt.*;

@RequiredArgsConstructor
public class JAppIntegrityActionRectInfo extends JPanel {
    private final IntegrityProvider integrityProvider;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(integrityProvider.isIntegrityStateTrue() ? Color.RED : Color.GRAY);
        g.fillRect(10, 10, 30, 30);
    }
}
