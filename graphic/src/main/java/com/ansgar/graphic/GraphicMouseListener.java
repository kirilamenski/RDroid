package com.ansgar.graphic;

import com.sun.istack.internal.NotNull;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class GraphicMouseListener implements MouseMotionListener, MouseListener {
    private int clickedXPos, xLastPos, xDraggedOffset;
    @NotNull
    private OnDraggedListener listener;

    GraphicMouseListener(@NotNull OnDraggedListener listener) {
        this.listener = listener;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        clickedXPos = e.getXOnScreen();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        xLastPos = xDraggedOffset;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int delta = clickedXPos - (e.getXOnScreen());
        xDraggedOffset = delta + xLastPos;
        if (xDraggedOffset < 0) xDraggedOffset = 0;
        listener.onDragged(xDraggedOffset);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    interface OnDraggedListener{
        void onDragged(int offset);
    }

}
