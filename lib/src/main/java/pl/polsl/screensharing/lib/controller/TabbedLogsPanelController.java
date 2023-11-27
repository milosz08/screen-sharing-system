/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import pl.polsl.screensharing.lib.file.FileUtils;
import pl.polsl.screensharing.lib.gui.fragment.JAppTabbedLogsPanel;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@RequiredArgsConstructor
public class TabbedLogsPanelController {
    private final JAppTabbedLogsPanel tabbedLogsPanel;

    public void moveUp() {
        final JScrollBar verticalScrollBar = tabbedLogsPanel.getScrollPane().getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMinimum());
    }

    public void moveDown() {
        final JScrollBar verticalScrollBar = tabbedLogsPanel.getScrollPane().getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
    }

    public void clearText() {
        final int result = JOptionPane.showConfirmDialog(tabbedLogsPanel, "Are you sure to clear console content?",
            "Please confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            final JTextArea textArea = JAppTabbedLogsPanel.textArea;
            textArea.setText(StringUtils.EMPTY);
        }
    }

    public void printToFile() {
        FileUtils.promptAndSaveFile("log.txt").ifPresent(file -> {
            try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(JAppTabbedLogsPanel.textArea.getText());
                JOptionPane.showMessageDialog(tabbedLogsPanel, "Logs saved to: " + file.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(tabbedLogsPanel, "An error occurred while saving logs to file!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
