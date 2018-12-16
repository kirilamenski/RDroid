package com.ansgar.rdroidpc.utils.listeners;

import com.android.chimpchat.core.TouchPressType;
import com.ansgar.rdroidpc.ui.frames.VideoFrame;

import java.awt.event.*;

import static com.ansgar.rdroidpc.utils.Converter.convertCoordinates;

public class FrameMouseListener implements MouseMotionListener, MouseListener, MouseWheelListener {

    private VideoFrame frame;

    public FrameMouseListener(VideoFrame frame) {
        this.frame = frame;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = (int) convertCoordinates(frame.getDeviceWidth(), frame.getWidth(), e.getX());
        int y = (int) convertCoordinates(frame.getDeviceHeight(), frame.getHeight(), e.getY());
        frame.getChimpDevice().touch(x, y, TouchPressType.MOVE);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
//        System.out.println("Moved: " + e.getX() + ", " + e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = (int) convertCoordinates(frame.getDeviceWidth(), frame.getWidth(), e.getX());
        int y = (int) convertCoordinates(frame.getDeviceHeight(), frame.getHeight(), e.getY());
        frame.getChimpDevice().touch(x, y, TouchPressType.DOWN_AND_UP);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = (int) convertCoordinates(frame.getDeviceWidth(), frame.getWidth(), e.getX());
        int y = (int) convertCoordinates(frame.getDeviceHeight(), frame.getHeight(), e.getY());
        frame.getChimpDevice().touch(x, y, TouchPressType.DOWN);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = (int) convertCoordinates(frame.getDeviceWidth(), frame.getWidth(), e.getX());
        int y = (int) convertCoordinates(frame.getDeviceHeight(), frame.getHeight(), e.getY());
        frame.getChimpDevice().touch(x, y, TouchPressType.UP);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        System.out.println("Entered: " + e.getX() + ", " + e.getY());
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        System.out.println("Exited: " + e.getX() + ", " + e.getY());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
//        System.out.println("Wheel moved: " + e.getX() + ", " + e.getY());
    }
}
