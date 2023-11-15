/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import pl.polsl.screensharing.host.controller.CaptureController;
import pl.polsl.screensharing.lib.gui.component.JAppIconButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Getter
public class CaptureFramelessWindow extends JFrame {
    private static final int NOTCH_WIDTH_PX = 300;
    private static final int BORDER_PX = 3;

    private final CaptureController captureController;

    private final JPanel bottomControls;
    private final JPanel centerTranslucentFrame;

    private final JAppIconButton startVideoStreamingButton;
    private final JAppIconButton stopVideoStreamingButton;

    public CaptureFramelessWindow(HostWindow hostWindow) {
        this.captureController = new CaptureController(hostWindow, this);

        setAlwaysOnTop(true);
        setSize(new Dimension(1280, 720));
        setBounds(100, 100, getWidth(), getHeight());
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setLayout(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        FrameDragListener frameDragListener = new FrameDragListener(this);
        addMouseListener(frameDragListener);
        addMouseMotionListener(frameDragListener);

        this.bottomControls = new JPanel();
        this.bottomControls.setLayout(new GridLayout(1, 2));
        this.bottomControls.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        this.bottomControls.setBounds(getWidth() / 2 - NOTCH_WIDTH_PX / 2, BORDER_PX, NOTCH_WIDTH_PX, 33);

        this.startVideoStreamingButton = new JAppIconButton(StringUtils.EMPTY, HostIcon.DEBUG_INTERACTIVE_WINDOW, true);
        this.stopVideoStreamingButton = new JAppIconButton(StringUtils.EMPTY, HostIcon.APPLICATION_ERROR, true);

        this.bottomControls.add(startVideoStreamingButton);
        this.bottomControls.add(stopVideoStreamingButton);

        this.centerTranslucentFrame = new JPanel();
        this.centerTranslucentFrame.setBorder(BorderFactory.createLineBorder(Color.GREEN, BORDER_PX));
        this.centerTranslucentFrame.setBackground(new Color(0, 0, 0, 0));
        this.centerTranslucentFrame.setBounds(0, 0, getWidth(), getHeight());

        add(centerTranslucentFrame);
        add(bottomControls);
    }

    // do poprawy
    public static class FrameDragListener extends MouseAdapter {
        private final JFrame frame;
        private Point mouseDownCompCoords = null;

        public FrameDragListener(JFrame frame) {
            this.frame = frame;
        }

        public void mouseReleased(MouseEvent e) {
            mouseDownCompCoords = null;
        }

        public void mousePressed(MouseEvent e) {
            mouseDownCompCoords = e.getPoint();
        }

        public void mouseDragged(MouseEvent e) {
            Point currCoords = e.getLocationOnScreen();
            frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
        }
    }
}
