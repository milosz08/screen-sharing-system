/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.fragment;

import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.gui.GridBagDrawer;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

abstract class AbstractScreenCaptureRightPanel extends JPanel {
    protected final HostState hostState;

    protected final EmptyBorder margin;
    protected final Insets gridInset;

    protected final JPanel mainPanel;
    protected final GridBagConstraints mainGridBag;
    protected final GridBagDrawer gridBagDrawer;

    protected AbstractScreenCaptureRightPanel(HostWindow hostWindow, String title) {
        hostState = hostWindow.getHostState();

        setLayout(new BorderLayout());

        margin = new EmptyBorder(7, 7, 7, 7);
        gridInset = new Insets(3, 3, 3, 3);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new CompoundBorder(new TitledBorder(title), margin));
        mainGridBag = new GridBagConstraints();

        gridBagDrawer = new GridBagDrawer(mainPanel, mainGridBag, gridInset);
        gridBagDrawer.prepareForVertical();
    }

    protected void drawToGridbag(JComponent component) {
        gridBagDrawer.addVertical(component);
    }
}
