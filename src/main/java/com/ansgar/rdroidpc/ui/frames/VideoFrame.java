package com.ansgar.rdroidpc.ui.frames;

import com.android.chimpchat.adb.AdbBackend;
import com.android.chimpchat.core.IChimpDevice;
import com.ansgar.rdoidpc.constants.AdbCommandEnum;
import com.ansgar.rdoidpc.entities.Device;
import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.utils.listeners.FrameMouseListener;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class VideoFrame extends JPanel {

    private final int Y_OFFSET = 48;
    private final float RESIZE_PERCENT = 0.85f;

    private JFrame frame;
    private Thread thread;
    private FrameGrabber frameGrabber;
    private BufferedImage currentImage;
    private Device device;
    private CommandExecutor commandExecutor;
    private FrameMouseListener listener;
    private IChimpDevice chimpDevice;

    private int imageWidth, imageHeight;
    private boolean isWindowUpdated;

    public VideoFrame(Device device) {
        this.device = device;
        this.commandExecutor = new CommandExecutor();
        this.chimpDevice = new AdbBackend().waitForConnection();

        frame = new JFrame(device.getDeviceName());
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // TODO
                stop();
                e.getWindow().dispose();
            }
        });

        initMouseListener();
    }

    public void start(AdbCommandEnum adbCommandEnum) {
        start(adbCommandEnum.getCommand());
    }

    public void startNewThread(AdbCommandEnum adbCommandEnum) {
        startNewThread(adbCommandEnum.getCommand());
    }

    public void startNewThread(String command) {
        if (thread != null) return;

        thread = new Thread(() -> start(command));
        thread.start();
    }

    public void start(String command) {
        CommandExecutor executor = new CommandExecutor();
        try {
            InputStream inputStream = executor.getInputStream(command);
            frameGrabber = new FFmpegFrameGrabber(inputStream);
            frameGrabber.start();

            Java2DFrameConverter converter = new Java2DFrameConverter();
            while (frameGrabber.grab() != null) {
                currentImage = converter.getBufferedImage(frameGrabber.grab());
                repaint();
            }

            stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            int width = (int) (currentImage.getWidth() * RESIZE_PERCENT);
            if (imageWidth != width) {
                imageWidth = width;
            }
            int height = (int) (currentImage.getHeight() * RESIZE_PERCENT);
            if (imageHeight != height) {
                imageHeight = height;
            }
            int x = -(imageWidth / 3 + 20);
            g2d.drawImage(currentImage, x, 0, imageWidth, imageHeight, this);
            g2d.dispose();

            if (!isWindowUpdated) updateWindowSize(x);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(480, 680);
    }

    public void stop() {
        stopGrabber();
        stopThread();
        stopFrame();
        chimpDevice.dispose();
    }

    private void updateWindowSize(int x) {
        frame.setBounds(300, 0, imageWidth - (imageWidth + x + 48), imageHeight + Y_OFFSET);
        frame.revalidate();
        isWindowUpdated = true;
    }

    private void stopGrabber() {
            try {
                frameGrabber.flush();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
    }

    private void stopThread() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            thread = null;
        }
    }

    private void stopFrame() {
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose();
        }
    }

    private void initMouseListener() {
        listener = new FrameMouseListener(this);
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }


    public IChimpDevice getChimpDevice() {
        return chimpDevice;
    }

    public int getDeviceWidth() {
        return device.getWidth();
    }

    public int getDeviceHeight() {
        return device.getHeight();
    }
}
