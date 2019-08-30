package com.ansgar.graphic;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class GraphicMouseListener implements MouseMotionListener, MouseListener {

    private int clickedXPos, xLastPos, xDraggedOffset;
    private OnDraggedListener listener;

    GraphicMouseListener(OnDraggedListener listener) {
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
        listener.onDragged(xDraggedOffset);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void setClickedXPos(int clickedXPos) {
        this.clickedXPos = clickedXPos;
    }

    public void setxLastPos(int xLastPos) {
        this.xLastPos = xLastPos;
    }

    public void setxDraggedOffset(int xDraggedOffset) {
        this.xDraggedOffset = xDraggedOffset;
    }

    interface OnDraggedListener{
        void onDragged(int offset);
    }

}
