package com.ansgar.rdroidpc.ui.components.videocomponent;

import com.android.chimpchat.core.IChimpDevice;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.enums.OrientationEnum;
import com.ansgar.rdroidpc.listeners.FrameMouseListener;
import org.bytedeco.javacv.FFmpegFrameGrabber;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class VideoComponent extends JPanel {

    private String deviceId;
    private FFmpegFrameGrabber frameGrabber;
    private IChimpDevice chimpDevice;
    private BufferedImage currentImage;
    private int coordinateX, imageWidth, imageHeight, deviceWidth, deviceHeight;
    private OrientationEnum currentOrientation;

    public VideoComponent(Device device, IChimpDevice chimpDevice) {
        this.deviceId = device.getDeviceId();
        this.deviceWidth = device.getWidth();
        this.deviceHeight = device.getHeight();
        this.chimpDevice = chimpDevice;
        initMouseListener();
    }

    public void displayImage(BufferedImage image) {
        currentImage = image;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(currentImage, coordinateX, 0, imageWidth, imageHeight, this);
            g2d.dispose();
        }
    }

    private void initMouseListener() {
        FrameMouseListener listener = new FrameMouseListener(this);
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    public void setCoordinateX(int coordinateX) {
        this.coordinateX = coordinateX;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getDeviceWidth() {
        return deviceWidth;
    }

    public int getDeviceHeight() {
        return deviceHeight;
    }

    public IChimpDevice getChimpDevice() {
        return chimpDevice;
    }

    public OrientationEnum getCurrentOrientation() {
        return currentOrientation;
    }

    public void setCurrentOrientation(OrientationEnum currentOrientation) {
        this.currentOrientation = currentOrientation;
    }
}
