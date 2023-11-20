/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

import javax.swing.*;
import java.awt.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
    public static String parseBytes(long bytes, String prefix, boolean isPerSec) {
        String formattedBytes;
        try {
            formattedBytes = String.format("%s%s", FileUtils.byteCountToDisplaySize(bytes),
                isPerSec ? "/s" : StringUtils.EMPTY);
        } catch (IllegalArgumentException ex) {
            formattedBytes = "unknow";
        }
        return String.format("%s: %s", prefix, formattedBytes);
    }

    public static String parseBytesPerSecToMegaBytes(long bytes, String prefix) {
        return String.format("%s: %.3f Mb/s", prefix, (double) bytes / (1024 * 1024));
    }

    public static String parseTime(long seconds, String prefix) {
        final String time = DurationFormatUtils.formatDuration(seconds * 1000, "HH:mm:ss");
        return String.format("%s time: %s", prefix, time);
    }

    public static Dimension calcSizeBaseAspectRatio(JComponent component, double aspectRatio) {
        int containerWidth = component.getWidth() - 10;
        int containerHeight = component.getHeight() - 10;

        int width, height;

        if ((double) containerWidth / containerHeight > aspectRatio) {
            height = containerHeight;
            width = (int) (height * aspectRatio);
        } else {
            width = containerWidth;
            height = (int) (width / aspectRatio);
        }
        return new Dimension(width, height);
    }
}
