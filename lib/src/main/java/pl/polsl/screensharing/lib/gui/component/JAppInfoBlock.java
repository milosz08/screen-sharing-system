/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.component;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;

public class JAppInfoBlock {
    @Getter
    private final JLabel label;
    @Getter
    private final JLabel value;
    private final Font font;

    public JAppInfoBlock(String label, String initValue) {
        this.label = new JLabel(label + ":");
        this.value = new JLabel(initValue);
        final Font labelFont = this.label.getFont();
        this.font = new Font(labelFont.getName(), Font.PLAIN, labelFont.getSize());
        this.label.setFont(font);
    }

    public JAppInfoBlock(String label) {
        this(label, StringUtils.EMPTY);
    }

    public void addToComponent(JComponent component) {
        component.add(label);
        component.add(value);
    }

    public void setText(String text) {
        value.setText(text);
    }
}
