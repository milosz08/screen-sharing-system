/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.fragment;

import lombok.Getter;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.controller.TabbedLogsPanelController;
import pl.polsl.screensharing.lib.gui.component.JAppIconButton;
import pl.polsl.screensharing.lib.gui.icon.LibIcon;

import javax.swing.*;
import java.awt.*;

@Getter
public class JAppTabbedLogsPanel extends JPanel {
    private final TabbedLogsPanelController tabbedLogsPanelController;
    private final AppType appType;

    public static JTextArea textArea;
    private final JScrollPane scrollPane;
    private final JToolBar rightButtonsToolbar;

    private final JAppIconButton moveToUpButton;
    private final JAppIconButton moveToDownButton;
    private final JAppIconButton clearButton;
    private final JAppIconButton printToFileButton;

    public JAppTabbedLogsPanel(AppType appType) {
        this.appType = appType;
        tabbedLogsPanelController = new TabbedLogsPanelController(this);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
        rightButtonsToolbar = new JToolBar();

        textArea.setEditable(false);
        textArea.setFont(new Font("monospaced", textArea.getFont().getStyle(), textArea.getFont().getSize()));

        rightButtonsToolbar.setLayout(new BoxLayout(rightButtonsToolbar, BoxLayout.Y_AXIS));
        rightButtonsToolbar.setBorder(BorderFactory.createEmptyBorder(5, 1, 5, 5));
        rightButtonsToolbar.setFloatable(false);

        moveToUpButton = new JAppIconButton("Move to up", LibIcon.UPLOAD, true);
        moveToDownButton = new JAppIconButton("Move to down", LibIcon.DOWNLOAD, true);
        clearButton = new JAppIconButton("Clear", LibIcon.DELETE, true);
        printToFileButton = new JAppIconButton("Print to file", LibIcon.PRINT, true);

        moveToUpButton.addActionListener(e -> tabbedLogsPanelController.moveUp());
        moveToDownButton.addActionListener(e -> tabbedLogsPanelController.moveDown());
        clearButton.addActionListener(e -> tabbedLogsPanelController.clearText());
        printToFileButton.addActionListener(e -> tabbedLogsPanelController.printToFile());

        addButtonWithSeparation(moveToUpButton);
        addButtonWithSeparation(moveToDownButton);
        rightButtonsToolbar.addSeparator();
        addButtonWithSeparation(clearButton);
        addButtonWithSeparation(printToFileButton);

        add(scrollPane, BorderLayout.CENTER);
        add(rightButtonsToolbar, BorderLayout.EAST);
    }

    private void addButtonWithSeparation(JAppIconButton button) {
        rightButtonsToolbar.add(button);
        rightButtonsToolbar.add(Box.createVerticalStrut(3));
    }
}
