package pl.polsl.screensharing.lib.gui;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractTabbedPanel extends JPanel {
    public AbstractTabbedPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
