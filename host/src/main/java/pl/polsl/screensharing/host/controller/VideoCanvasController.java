/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import pl.polsl.screensharing.host.gfx.ScreenCapturer;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostIcon;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.fragment.VideoCanvas;
import pl.polsl.screensharing.host.view.tabbed.TabbedScreenFramePanel;
import pl.polsl.screensharing.lib.Utils;
import pl.polsl.screensharing.lib.gui.file.FileUtils;
import pl.polsl.screensharing.lib.thread.AbstractPerTickRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

@Slf4j
public class VideoCanvasController extends AbstractPerTickRunner {
    private final HostState hostState;
    private final VideoCanvas videoCanvas;
    private final TabbedScreenFramePanel tabbedScreenFramePanel;
    private final ScreenCapturer screenCapturer;
    private final Optional<Image> cursorImageOptional;
    private final Point cursorPos;

    @Getter
    private BufferedImage rawImage;
    private BufferedImage renderImage;

    public VideoCanvasController(
        HostWindow hostWindow, VideoCanvas videoCanvas, TabbedScreenFramePanel tabbedScreenFramePanel
    ) {
        super(30);
        hostState = hostWindow.getHostState();
        this.videoCanvas = videoCanvas;
        this.tabbedScreenFramePanel = tabbedScreenFramePanel;
        screenCapturer = new ScreenCapturer();
        cursorPos = new Point();
        cursorImageOptional = FileUtils
            .getImageIconFromResources(getClass(), HostIcon.CURSOR)
            .map(ImageIcon::getImage);
    }

    public void onResizeWithAspectRatio() {
        final JPanel videoFrameHolder = tabbedScreenFramePanel.getVideoFrameHolder();
        final Rectangle screenSize = videoCanvas.getGraphicsDevice().getDefaultConfiguration().getBounds();
        final Dimension size = Utils
            .calcSizeBaseAspectRatio(videoFrameHolder, screenSize.getWidth() / screenSize.getHeight());
        tabbedScreenFramePanel.getVideoCanvas().setPreferredSize(size);
        tabbedScreenFramePanel.getDisableScreenPanel().setPreferredSize(size);
        videoFrameHolder.revalidate();
    }

    public BufferedImage grabAndDrawCursor(GraphicsDevice graphicsDevice) {
        final BufferedImage rawImage = screenCapturer.getScreenShutterState(graphicsDevice);
        final Graphics graphics = rawImage.getGraphics();

        if (videoCanvas.getIsCursorShowing().get() && cursorImageOptional.isPresent()) {
            graphics.drawImage(cursorImageOptional.get(), cursorPos.x, cursorPos.y, null);
        }
        graphics.dispose();
        return rawImage;
    }

    public void renderContent(Graphics graphics) {
        if (renderImage != null) {
            graphics.drawImage(renderImage, 0, 0, videoCanvas);
        }
    }

    @Override
    public void onTickUpdate() {
        if (!videoCanvas.getIsScreenShowingForParticipants().get()) {
            return;
        }
        final GraphicsDevice graphicsDevice = videoCanvas.getGraphicsDevice();
        final int width = videoCanvas.getWidth();
        final int height = videoCanvas.getHeight();

        cursorPos.setLocation(screenCapturer.getCurrentCursorPosForDevice(graphicsDevice));
        rawImage = grabAndDrawCursor(graphicsDevice);
        renderImage = Scalr.resize(rawImage, width, height);

        videoCanvas.repaint();
    }

    @Override
    public void onUpdateFpsState(int fpsValue) {
        hostState.updateRealFpsBuffer(fpsValue);
    }
}
