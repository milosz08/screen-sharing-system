/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui;

import pl.polsl.screensharing.lib.AppType;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public abstract class AbstractRootFrame extends JFrame {
    private final AppType appType;
    private final Dimension size;
    private final JPanel rootPanel;
    private final Optional<Image> imageIconOptional;

    protected AbstractRootFrame(
        AppType appType,
        Class<?> frameClazz
    ) {
        this.rootPanel = new JPanel();
        this.appType = appType;
        this.imageIconOptional = appType.getIconPath(frameClazz);
        this.size = appType.getRootWindowSize();
    }

    public void guiInitAndShow() {
        rootPanel.setBounds(0, 0, getWidth(), getHeight());
        imageIconOptional.ifPresent(this::setIconImage);

        setSize(size);
        setMinimumSize(size);
        setLocation(getMotherScreenCenter());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new GuiWindowAdapter(this));
        setTitle(appType.getRootWindowTitle());
        setLayout(new BorderLayout());
        add(rootPanel, BorderLayout.CENTER);
        extendsFrame(this, rootPanel);
        setVisible(true);
    }

    private Point getMotherScreenCenter() {
        final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        final int x = dimension.width / 2 - getWidth() / 2;
        final int y = dimension.height / 2 - getHeight() / 2;
        return new Point(x, y);
    }

    protected abstract void extendsFrame(JFrame frame, JPanel rootPanel);
}
