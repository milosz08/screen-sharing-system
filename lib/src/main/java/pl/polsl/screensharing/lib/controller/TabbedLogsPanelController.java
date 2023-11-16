/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import pl.polsl.screensharing.lib.gui.fragment.JAppTabbedLogsPanel;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

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
            "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            final JTextArea textArea = JAppTabbedLogsPanel.textArea;
            textArea.setText(StringUtils.EMPTY);
        }
    }

    public void printToFile() {
        final FastDateFormat targetFormat = FastDateFormat.getInstance("MMddyyyyHHmmssSSS");
        final String appName = tabbedLogsPanel.getAppType().getRootWindowName().toLowerCase();
        final String logFileName = String.format("%s-%slog.txt", targetFormat.format(new Date()), appName);

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(logFileName));

        final int result = fileChooser.showSaveDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        final File fileToSave = fileChooser.getSelectedFile();
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
            writer.write(JAppTabbedLogsPanel.textArea.getText());
            JOptionPane.showMessageDialog(tabbedLogsPanel, "Logs saved to: " + fileToSave.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(tabbedLogsPanel, "An error occurred while saving logs to file!",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
