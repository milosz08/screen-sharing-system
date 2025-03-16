package pl.polsl.screensharing.lib.gui;

import lombok.Getter;
import pl.polsl.screensharing.lib.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public abstract class AbstractBottomInfobar extends JPanel {
    protected final JPanel leftCompoundPanel;
    protected final JPanel rightCompoundPanel;
    protected final JPanel stateCompoundPanel;

    @Getter
    protected final JLabel memoryUsageLabel;

    protected final Border marginRight;

    protected AbstractBottomInfobar() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        leftCompoundPanel = new JPanel();
        rightCompoundPanel = new JPanel();
        stateCompoundPanel = new JPanel();

        marginRight = BorderFactory.createEmptyBorder(0, 0, 0, 20);

        memoryUsageLabel = new JLabel(Utils.parseBytes(0, "Memory", false));

        leftCompoundPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rightCompoundPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        stateCompoundPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        stateCompoundPanel.setBorder(marginRight);
        memoryUsageLabel.setBorder(marginRight);
    }

    protected void addPanels() {
        add(leftCompoundPanel);
        add(rightCompoundPanel);
    }
}
