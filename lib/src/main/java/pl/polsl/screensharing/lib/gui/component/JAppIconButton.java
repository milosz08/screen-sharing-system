/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.component;

import pl.polsl.screensharing.lib.AppIcon;
import pl.polsl.screensharing.lib.gui.file.FileUtils;

import javax.swing.*;

public class JAppIconButton extends JButton {
    public JAppIconButton(String text, AppIcon iconName) {
        this(text, iconName, false);
    }

    public JAppIconButton(String text, AppIcon iconName, boolean setDesciption) {
        if (setDesciption) {
            setToolTipText(text);
        } else {
            setText(text);
        }
        setFocusable(false);
        FileUtils.getImageIconFromResources(getClass(), iconName).ifPresent(this::setIcon);
    }

    public JAppIconButton(String text, AppIcon iconName, boolean setDesciption, boolean isEnabled) {
        this(text, iconName, setDesciption);
        setEnabled(isEnabled);
    }
}
