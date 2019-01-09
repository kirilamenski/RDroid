package com.ansgar.rdroidpc.listeners;

import com.android.chimpchat.core.TouchPressType;
import com.ansgar.rdroidpc.ui.frames.VideoFrame;

import java.awt.event.*;

import static com.ansgar.rdroidpc.utils.DimensionUtils.convertCoordinates;

public class FrameMouseListener implements MouseMotionListener, MouseListener {

    private VideoFrame frame;

    public FrameMouseListener(VideoFrame frame) {
        this.frame = frame;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = (int) convertCoordinates(frame.getDeviceWidth(), frame.getWidth(), e.getX());
        int y = (int) convertCoordinates(frame.getDeviceHeight(), frame.getFrameHeight(), e.getY());
        frame.getChimpDevice().touch(x, y, TouchPressType.MOVE);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = (int) convertCoordinates(frame.getDeviceWidth(), frame.getWidth(), e.getX());
        int y = (int) convertCoordinates(frame.getDeviceHeight(), frame.getFrameHeight(), e.getY());
        frame.getChimpDevice().touch(x, y, TouchPressType.DOWN);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = (int) convertCoordinates(frame.getDeviceWidth(), frame.getWidth(), e.getX());
        int y = (int) convertCoordinates(frame.getDeviceHeight(), frame.getFrameHeight(), e.getY());
        frame.getChimpDevice().touch(x, y, TouchPressType.UP);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
