/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui;

import lombok.Getter;
import pl.polsl.screensharing.lib.Parser;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public abstract class AbstractBottomInfobar extends JPanel {
    protected final JPanel leftPanel;
    protected final JPanel rightPanel;
    protected final JPanel stateCompound;

    @Getter
    protected final JLabel memoryUsageLabel;

    protected final Border marginRight;

    protected AbstractBottomInfobar() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        this.leftPanel = new JPanel();
        this.rightPanel = new JPanel();
        this.stateCompound = new JPanel();

        this.marginRight = BorderFactory.createEmptyBorder(0, 0, 0, 20);

        this.memoryUsageLabel = new JLabel(Parser.parseBytes(0, "Memory", false));

        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        stateCompound.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        stateCompound.setBorder(marginRight);
        memoryUsageLabel.setBorder(marginRight);
    }

    protected void addPanels() {
        add(leftPanel);
        add(rightPanel);
    }
}
