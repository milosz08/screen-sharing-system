/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public abstract class AbstractFrame extends JFrame {
    private final String title;
    private final Dimension size;
    private final String iconPath;
    private final Class<? extends AbstractFrame> frameClazz;

    private final JPanel rootPanel = new JPanel();

    protected AbstractFrame(
        String title,
        int width,
        int height,
        String iconPath,
        Class<? extends AbstractFrame> frameClazz
    ) {
        this.title = title;
        this.size = new Dimension(width, height);
        this.iconPath = iconPath;
        this.frameClazz = frameClazz;
    }

    public void guiInit(boolean isVisible) {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new GuiWindowAdapter(this));
        setSize(size);
        setMinimumSize(size);
        setTitle(title);
        setLayout(new BorderLayout());
        extractIconFromResources();
        rootPanel.setBounds(0, 0, getWidth(), getHeight());
        add(rootPanel, BorderLayout.CENTER);
        setVisible(isVisible);
    }

    public void guiInit() {
        guiInit(true);
    }

    private void extractIconFromResources() {
        final URL url = frameClazz.getClassLoader().getResource(iconPath);
        if (url != null) {
            setIconImage(new ImageIcon(url).getImage());
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
