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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Parser {
    public static String parseBytes(long bytes, String prefix, boolean isPerSec) {
        String formattedBytes;
        try {
            formattedBytes = String.format("%s%s", FileUtils.byteCountToDisplaySize(bytes),
                isPerSec ? "/s" : StringUtils.EMPTY);
        } catch (IllegalArgumentException ex) {
            formattedBytes = "unknow";
        }
        return String.format("%s: %s", prefix, formattedBytes.toLowerCase());
    }

    public static String parseBytesPerSecState(long bytes, String prefix) {
        return Parser.parseBytes(bytes, prefix, true);
    }

    public static String parseTime(long seconds, String prefix) {
        final String time = DurationFormatUtils.formatDuration(seconds * 1000, "HH:mm:ss");
        return String.format("%s time: %s", prefix, time);
    }
}
