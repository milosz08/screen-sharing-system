package pl.polsl.screensharing.lib.gui;

import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;

@RequiredArgsConstructor
public class GridBagDrawer {
    private final JPanel panel;
    private final GridBagConstraints gridBag;
    private final Insets insets;

    public void drawGridBagLabels(JComponent... components) {
        gridBag.gridx = 0;
        gridBag.gridy = 0;
        gridBag.insets = insets;
        gridBag.anchor = GridBagConstraints.NORTHEAST;
        for (final JComponent component : components) {
            panel.add(component, gridBag);
            gridBag.gridy++;
        }
    }

    public void drawGridBagInputs(JComponent... components) {
        gridBag.gridx++;
        gridBag.gridy = 0;
        gridBag.weightx = 1;
        gridBag.fill = GridBagConstraints.HORIZONTAL;
        for (final JComponent component : components) {
            panel.add(component, gridBag);
            gridBag.gridy++;
        }
    }

    public void prepareForVertical() {
        gridBag.gridx = 0;
        gridBag.gridy = 0;
        gridBag.weightx = 1;
        gridBag.fill = GridBagConstraints.HORIZONTAL;
        gridBag.anchor = GridBagConstraints.NORTHEAST;
    }

    public void addVertical(JComponent component) {
        gridBag.insets = insets;
        panel.add(component, gridBag);
        gridBag.gridy++;
    }
}
