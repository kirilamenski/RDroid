package com.ansgar.rdroidpc.ui.components.videocomponent;

import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.enums.OrientationEnum;
import com.ansgar.rdroidpc.listeners.FrameMouseListener;
import com.ansgar.rdroidpc.listeners.OnDeviceInputListener;
import org.bytedeco.javacv.FFmpegFrameGrabber;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class VideoComponent extends JPanel {

    private String deviceId;
    private FFmpegFrameGrabber frameGrabber;
    private BufferedImage currentImage;
    private int coordinateX, imageWidth, imageHeight, deviceWidth, deviceHeight;
    private OrientationEnum currentOrientation;
    private OnDeviceInputListener listener;

    public VideoComponent(Device device, OnDeviceInputListener listener) {
        this.deviceId = device.getDeviceId();
        this.deviceWidth = device.getWidth();
        this.deviceHeight = device.getHeight();
        this.listener = listener;
        initMouseListener();
    }

    public void start(BufferedImage image) {
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

    public void initMouseListener() {
        FrameMouseListener mouseListener = new FrameMouseListener(this, listener);
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
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

    public OrientationEnum getCurrentOrientation() {
        return currentOrientation;
    }

    public void setCurrentOrientation(OrientationEnum currentOrientation) {
        this.currentOrientation = currentOrientation;
    }

}
