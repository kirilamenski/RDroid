package com.ansgar.rdroidpc.listeners;

import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.TouchPressType;
import com.ansgar.rdroidpc.enums.OrientationEnum;
import com.ansgar.rdroidpc.ui.components.videocomponent.VideoComponent;

import java.awt.event.*;

import static com.ansgar.rdroidpc.utils.DimensionUtils.convertCoordinates;

public class FrameMouseListener implements MouseMotionListener, MouseListener {

    private VideoComponent component;

    public FrameMouseListener(VideoComponent component) {
        this.component = component;
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
        int _x = (int) convertCoordinates(getOriginalScreenSize()[0], component.getWidth(), x);
        int _y = (int) convertCoordinates(getOriginalScreenSize()[1], component.getHeight(), y);

        IChimpDevice chimpDevice = component.getChimpDevice();
        if (chimpDevice != null) component.getChimpDevice().touch(_x, _y, type);
    }

    private int[] getOriginalScreenSize() {
        int[] originalSize = new int[2];
        if (component.getCurrentOrientation() == OrientationEnum.PORTRAIT) {
            originalSize[0] = component.getDeviceWidth();
            originalSize[1] = component.getDeviceHeight();
        } else {
            originalSize[0] = component.getDeviceHeight();
            originalSize[1] = component.getDeviceWidth();
        }
        return originalSize;
    }

}
