/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui;

import pl.polsl.screensharing.lib.AppType;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public abstract class AbstractPopupDialog extends JDialog {
    private final JFrame rootFrame;
    private final JPanel rootPanel;
    private final String title;
    private final Dimension size;
    private final Optional<Image> iconImageOptional;

    public AbstractPopupDialog(
        AppType appType,
        int width,
        int height,
        String title,
        AbstractRootFrame rootFrame,
        Class<?> frameClazz
    ) {
        this.rootPanel = new JPanel();
        this.size = new Dimension(width, height);
        this.rootFrame = rootFrame;
        this.title = title;
        this.iconImageOptional = appType.getIconPath(frameClazz);
    }

    protected void initDialogGui(boolean fixedToContent) {
        iconImageOptional.ifPresent(this::setIconImage);
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rootPanel.setLayout(new BorderLayout(10, 10));

        setModal(true);
        setSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        setLocationRelativeTo(rootFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle(title);
        extendsDialog(this, rootPanel);
        add(rootPanel);
        if (fixedToContent) {
            pack();
        }
    }

    public void closeWindow() {
        setVisible(false);
        dispose();
    }

    protected abstract void extendsDialog(JDialog dialog, JPanel rootPanel);
}
