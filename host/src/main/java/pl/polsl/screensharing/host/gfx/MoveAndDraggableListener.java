/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.gfx;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MoveAndDraggableListener extends MouseAdapter {
    private static final int BORDER_SIZE = 10;

    private final JComponent root;
    private final JComponent parent;
    private final Point mouseCoords;
    private final Point screenCoords;

    private Dimension minSize;
    private boolean resizing;
    private boolean dragging;
    private boolean isDisabledResizing;

    public MoveAndDraggableListener(JComponent root, JComponent parent) {
        this.root = root;
        this.parent = parent;
        mouseCoords = new Point();
        screenCoords = new Point();
        isDisabledResizing = false;
    }

    public void toogleActionResizingCapability(boolean isDisabledResizing) {
        this.isDisabledResizing = isDisabledResizing;
    }

    public void setMinSize(Dimension minSize) {
        this.minSize = minSize;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isInResizeArea(e)) {
            resizing = true;
            mouseCoords.x = e.getX();
            mouseCoords.y = e.getY();
        } else if (isInDragArea(e)) {
            screenCoords.x = e.getXOnScreen();
            screenCoords.y = e.getYOnScreen();
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        resizing = false;
        dragging = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (resizing && !isDisabledResizing) {
            int deltaX = e.getX() - mouseCoords.x;
            int deltaY = e.getY() - mouseCoords.y;

            final Dimension size = root.getSize();
            size.width += deltaX;
            size.height += deltaY;

            size.width = Math.min(size.width, parent.getWidth() - root.getX());
            size.height = Math.min(size.height, parent.getHeight() - root.getY());
            size.width = Math.max(size.width, minSize.width);
            size.height = Math.max(size.height, minSize.height);

            root.setSize(size);
            root.repaint();

            mouseCoords.x = e.getX();
            mouseCoords.y = e.getY();
        } else if (dragging) {
            int deltaX = e.getXOnScreen() - screenCoords.x;
            int deltaY = e.getYOnScreen() - screenCoords.y;

            final Point pos = root.getLocation();
            pos.x += deltaX;
            pos.y += deltaY;

            pos.x = Math.max(pos.x, 0);
            pos.y = Math.max(pos.y, 0);
            pos.x = Math.min(pos.x, parent.getWidth() - root.getWidth());
            pos.y = Math.min(pos.y, parent.getHeight() - root.getHeight());

            root.setLocation(pos);
            root.repaint();

            screenCoords.x = e.getXOnScreen();
            screenCoords.y = e.getYOnScreen();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Cursor cursor = Cursor.getDefaultCursor();
        if (isInResizeArea(e)) {
            cursor = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
        } else if (isInDragArea(e)) {
            cursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
        }
        root.setCursor(cursor);
    }

    private boolean isInResizeArea(MouseEvent e) {
        return e.getX() > root.getWidth() - BORDER_SIZE
            && e.getY() > root.getHeight() - BORDER_SIZE && !isDisabledResizing;
    }

    private boolean isInDragArea(MouseEvent e) {
        return e.getX() < root.getWidth() - BORDER_SIZE && e.getY() < root.getHeight() - BORDER_SIZE;
    }
}
