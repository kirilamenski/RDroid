package com.ansgar.rdroidpc.ui.frames;

import com.ansgar.rdoidpc.constants.AdbCommandEnum;
import com.ansgar.rdroidpc.commands.CommandExecutor;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CustomCanvasFrame extends CanvasFrame implements MouseMotionListener, MouseListener, MouseWheelListener {

    private Thread thread;
    private FrameGrabber frameGrabber;
    private BufferedImage currentImage;

    public CustomCanvasFrame(String title) {
        super(title);

        setLayout(new FlowLayout());

        addMouseListener(this);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
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
            while (frameGrabber.grab() != null) {
                showImage(frameGrabber.grab());
            }

            stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        stopGrabber();
        stopThread();
        dispose();
    }

    private void stopGrabber() {
        if (frameGrabber != null) {
            try {
                frameGrabber.flush();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void stopThread() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            thread = null;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("Dragged: " + e.getX() + ", " + e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        System.out.println("Moved: " + e.getX() + ", " + e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Clicked: " + e.getX() + ", " + e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("Pressed: " + e.getX() + ", " + e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Released: " + e.getX() + ", " + e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("Entered: " + e.getX() + ", " + e.getY());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("Exited: " + e.getX() + ", " + e.getY());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        System.out.println("Wheel moved: " + e.getX() + ", " + e.getY());
    }
}

