/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.fragment;

import lombok.Getter;
import pl.polsl.screensharing.host.controller.TopToolbarController;
import pl.polsl.screensharing.host.view.HostIcon;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.gui.component.JAppIconButton;

import javax.swing.*;

@Getter
public class TopToolbar extends JToolBar {
    private final JAppIconButton createConnectionButton;

    private final TopToolbarController controller;

    public TopToolbar(HostWindow hostWindow) {
        this.controller = new TopToolbarController(hostWindow);

        this.createConnectionButton = new JAppIconButton("Connection settings", AppIcon.SERVER_SETTINGS, true);

        this.createConnectionButton.addActionListener(e -> controller.openMakeConnectionWindow());

        addButtonWithSeparation(createConnectionButton);

        setFloatable(false);
    }

    private void addButtonWithSeparation(JAppIconButton button) {
        add(button);
        add(Box.createHorizontalStrut(5));
    }
}
