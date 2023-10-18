/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.component;

import pl.polsl.screensharing.lib.AppIcon;
import pl.polsl.screensharing.lib.gui.file.FileUtils;

import javax.swing.*;

public class JAppMenuIconItem extends JMenuItem {
    public JAppMenuIconItem(String text, AppIcon iconName) {
        super(text);
        FileUtils.getImageIconFromResources(JAppMenuIconItem.class, iconName).ifPresent(this::setIcon);
    }

    public JAppMenuIconItem(String text, AppIcon iconName, boolean isEnabled) {
        this(text, iconName);
        setEnabled(isEnabled);
    }
}
