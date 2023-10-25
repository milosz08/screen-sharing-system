/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view.fragment;

import pl.polsl.screensharing.host.controller.TopMenuBarController;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.AppIcon;
import pl.polsl.screensharing.lib.gui.component.JAppMenuIconItem;

import javax.swing.*;

public class TopMenuBar extends JMenuBar {
    private final TopMenuBarController controller;

    private final JMenu connectMenu;
    private final JMenu helpMenu;

    private final JAppMenuIconItem connectionSettingsMenuItem;
    private final JAppMenuIconItem aboutMenuItem;
    private final JAppMenuIconItem licenseMenuItem;

    private final JAppMenuIconItem[] connectMenuItems;
    private final JAppMenuIconItem[] helpMenuItems;

    public TopMenuBar(HostWindow hostWindow) {
        this.controller = new TopMenuBarController(hostWindow);

        this.connectMenu = new JMenu("Connect");
        this.helpMenu = new JMenu("Help");

        this.connectionSettingsMenuItem = new JAppMenuIconItem("Connection settings", AppIcon.SERVER_SETTINGS);

        this.aboutMenuItem = new JAppMenuIconItem("About", AppIcon.CODE_INFORMATION);
        this.licenseMenuItem = new JAppMenuIconItem("License", AppIcon.CODE_INFORMATION_RULE);

        this.connectMenuItems = new JAppMenuIconItem[]{
            connectionSettingsMenuItem
        };

        this.helpMenuItems = new JAppMenuIconItem[]{
            aboutMenuItem,
            licenseMenuItem
        };

        this.aboutMenuItem.addActionListener(e -> controller.openAboutProgramSection());
        this.licenseMenuItem.addActionListener(e -> controller.openLicenseSection());

        addMenuItems(connectMenu, connectMenuItems);
        addMenuItems(helpMenu, helpMenuItems);

        add(connectMenu);
        add(helpMenu);
    }

    private void addMenuItems(JMenu menu, JMenuItem[] items) {
        for (final JMenuItem item : items) {
            menu.add(item);
        }
    }

    public void setConnectionButtonsState(boolean onIsConnect) {
        this.connectionSettingsMenuItem.setEnabled(!onIsConnect);
    }
}
