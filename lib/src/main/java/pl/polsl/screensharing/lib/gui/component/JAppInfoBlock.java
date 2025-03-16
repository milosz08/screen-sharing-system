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
    private final Font normalFont;
    private final Font italicFont;

    public JAppInfoBlock(String label, String initValue) {
        this.label = new JLabel(label + ":");
        this.value = new JLabel(initValue);
        final Font labelFont = this.label.getFont();
        this.normalFont = new Font(labelFont.getName(), Font.PLAIN, labelFont.getSize());
        this.italicFont = new Font(labelFont.getName(), Font.ITALIC, labelFont.getSize());
        this.label.setFont(normalFont);
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

    public void setFontToValue(boolean isItalic) {
        value.setFont(isItalic ? italicFont : normalFont);
    }
}
