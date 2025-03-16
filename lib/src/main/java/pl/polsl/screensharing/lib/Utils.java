package pl.polsl.screensharing.lib;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
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

    public static Dimension calcHeightBaseWidthAndAR(int width, Rectangle bounds) {
        final double aspectRatio = bounds.getWidth() / bounds.getHeight();
        final int height = (int) (width / aspectRatio);
        return new Dimension(width, height);
    }

    public static boolean checkIfSizeNotExact(Rectangle first, Rectangle second) {
        return first.width != second.width || first.height != second.height;
    }

    public static Rectangle scaleElementUp(int width, int height, Rectangle bounds, Rectangle scale) {
        final double xS = ((double) scale.x / width) * bounds.getWidth();
        final double yS = ((double) scale.y / height) * (int) bounds.getHeight();
        final double widthS = ((double) scale.width / width) * (int) bounds.getWidth();
        final double heightS = ((double) scale.height / height) * (int) bounds.getHeight();
        return new Rectangle((int) xS, (int) yS, (int) widthS, (int) heightS);
    }

    public static double calcAspectRatio(BufferedImage bufferedImage) {
        double aspectRatio = (double) bufferedImage.getWidth() / bufferedImage.getHeight();
        return Math.round(aspectRatio * 100) / 100.0;
    }

    public static String getMachineAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            throw new UnoperableException(ex);
        }
    }

    public static int getRandomPortOrDefault(int defaultPort) {
        int selectedPort = defaultPort;
        for (int port = 1024; port <= 65535; port++) {
            try (
                final ServerSocket ignored = new ServerSocket(port);
                final DatagramSocket ignored1 = new DatagramSocket(port)
            ) {
                selectedPort = port;
                break;
            } catch (IOException ignore) {
            }
        }
        return selectedPort;
    }

    public static InetSocketAddress extractAddrDetails(String merged) {
        final int separatorPos = merged.indexOf(':');
        return new InetSocketAddress(
            merged.substring(0, separatorPos),
            Integer.parseInt(merged.substring(separatorPos + 1))
        );
    }

    public static void generateThreadUsagePerTick() {
        final AtomicInteger previousCount = new AtomicInteger(0);
        final Timer timer = new Timer(12_000, e -> {
            if (previousCount.get() != Thread.activeCount() || previousCount.get() == 0) {
                log.info("All current active threads: {}", Thread.activeCount());
            }
            previousCount.set(Thread.activeCount());
        });
        timer.start();
    }
}
