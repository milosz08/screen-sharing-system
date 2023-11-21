/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.gfx;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.lib.UnoperableException;

import java.awt.*;
import java.awt.image.BufferedImage;

@Slf4j
public class ScreenCapturer {
    private final Robot robot;
    private final Point cursorPoint;

    public ScreenCapturer() {
        robot = instantiateRobot();
        cursorPoint = new Point();
    }

    public BufferedImage getScreenShutterState(GraphicsDevice graphicsDevice) {
        final Rectangle rectangle = graphicsDevice.getDefaultConfiguration().getBounds();
        return robot.createScreenCapture(rectangle);
    }

    public Point getCurrentCursorPosForDevice(GraphicsDevice graphicsDevice) {
        final PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        final Point cursorLocation = pointerInfo.getLocation();
        final Rectangle screenBounds = graphicsDevice.getDefaultConfiguration().getBounds();

        final int mouseXOnScreen = cursorLocation.x - (int) screenBounds.getX();
        final int mouseYOnScreen = cursorLocation.y - (int) screenBounds.getY();

        cursorPoint.setLocation(mouseXOnScreen, mouseYOnScreen);
        return cursorPoint;
    }

    private Robot instantiateRobot() {
        Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException | SecurityException ex) {
            throw new UnoperableException(ex);
        }
        return robot;
    }
}
