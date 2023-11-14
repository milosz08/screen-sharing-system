/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.fragment;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pl.polsl.screensharing.lib.gui.file.Alignment;
import pl.polsl.screensharing.lib.gui.file.FileUtils;

import javax.swing.*;
import java.time.LocalDate;

@Slf4j
public class JAppLicensePanel extends JPanel {
    @Getter
    private final JScrollPane scrollPane;
    private final JLabel label;

    public JAppLicensePanel() {
        this.scrollPane = new JScrollPane(this);
        this.label = new JLabel(loadHtmlLicenseContent());

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(label);
    }

    private String loadHtmlLicenseContent() {
        try {
            return FileUtils
                .loadAndWrapAsHtmlContent("license.txt", getClass(), Alignment.CENTER)
                .replace("[year]", String.valueOf(LocalDate.now().getYear()));
        } catch (Exception ex) {
            log.error("Unable to load license.txt file from assets resource directory.");
            System.exit(-1);
        }
        return StringUtils.EMPTY;
    }
}
