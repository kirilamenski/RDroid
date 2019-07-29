package com.ansgar.rdroidpc.listeners;

import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.TouchPressType;
import com.ansgar.rdroidpc.enums.OrientationEnum;
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
        handleMouseListener(e.getX(), e.getY(), TouchPressType.MOVE);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        handleMouseListener(e.getX(), e.getY(), TouchPressType.DOWN);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        handleMouseListener(e.getX(), e.getY(), TouchPressType.UP);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void handleMouseListener(int x, int y, TouchPressType type) {
        int _x = (int) convertCoordinates(getOriginalScreenSize()[0], frame.getFrameWidth(), x);
        int _y = (int) convertCoordinates(getOriginalScreenSize()[1], frame.getFrameHeight(), y);
        IChimpDevice chimpDevice = frame.getChimpDevice();
        if (chimpDevice != null) {
            frame.getChimpDevice().touch(_x, _y, type);
        }
    }

    private int[] getOriginalScreenSize() {
        int[] originalSize = new int[2];
        if (frame.getCurrentOrientation() == OrientationEnum.PORTRAIT) {
            originalSize[0] = frame.getDeviceWidth();
            originalSize[1] = frame.getDeviceHeight();
        } else {
            originalSize[0] = frame.getDeviceHeight();
            originalSize[1] = frame.getDeviceWidth();
        }
        return originalSize;
    }

}
