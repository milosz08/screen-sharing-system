/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.component;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class JAppPasswordTextField extends JPasswordField {
    public JAppPasswordTextField(int columns) {
        super(columns);
        setEchoChar('*');
        setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)));
    }

    public void toggleVisibility(boolean isVisible) {
        if (isVisible) {
            setEchoChar((char) 0);
        } else {
            setEchoChar('*');
        }
    }
}
