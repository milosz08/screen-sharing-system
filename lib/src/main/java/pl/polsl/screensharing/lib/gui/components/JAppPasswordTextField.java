/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.components;

import javax.swing.*;

public class JAppPasswordTextField extends JPasswordField {
    public JAppPasswordTextField(int columns) {
        super(columns);
        setEchoChar('*');
    }

    public void toggleVisibility(boolean isVisible) {
        if (isVisible) {
            setEchoChar((char) 0);
        } else {
            setEchoChar('*');
        }
    }
}
