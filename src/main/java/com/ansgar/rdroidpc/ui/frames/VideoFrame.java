package com.ansgar.rdroidpc.ui.frames;

import com.android.chimpchat.adb.AdbBackend;
import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.TouchPressType;
import com.ansgar.rdoidpc.constants.AdbCommandEnum;
import com.ansgar.rdoidpc.constants.AdbKeyCode;
import com.ansgar.rdoidpc.constants.Colors;
import com.ansgar.rdoidpc.constants.DimensionConst;
import com.ansgar.rdoidpc.entities.Device;
import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.ui.components.NavigationBottomPanel;
import com.ansgar.rdroidpc.utils.listeners.FrameMouseListener;
import com.ansgar.rdroidpc.utils.listeners.KeyboardListener;
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

    private JFrame frame;
    private Thread thread;
    private FrameGrabber frameGrabber;
    private BufferedImage currentImage;
    private Device device;
    private IChimpDevice chimpDevice;
    private AdbBackend adbBackend;
    private CommandExecutor commandExecutor;

    private int imageWidth, imageHeight;
    private boolean isWindowUpdated;

    public VideoFrame(Device device) {
        this.device = device;
        this.adbBackend = new AdbBackend();
        this.chimpDevice = adbBackend.waitForConnection(2147483647L, device.getDeviceId());

        setLayout(null);
        initFrame(device.getDeviceName());
        initMouseListener();
        initKeyboardListener();
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
        commandExecutor = new CommandExecutor();
        try {
            InputStream inputStream = commandExecutor.getInputStream(command);
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
            int width = (int) (currentImage.getWidth() * 0.5f);
            if (imageWidth != width) {
                imageWidth = width;
            }
            int height = (int) (currentImage.getHeight() * 0.5f);
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
        if (commandExecutor != null) commandExecutor.destroy();
        if (adbBackend != null) adbBackend.shutdown();
    }

    private void updateWindowSize(int x) {
        frame.setBounds(300, 0,
                imageWidth - (imageWidth + x + 48),
                imageHeight + Y_OFFSET + DimensionConst.NAVIGATION_PANEL_HEIGHT);
        add(initNavigationPanel());
        frame.revalidate();
        isWindowUpdated = true;
    }

    private void stopGrabber() {
        try {
            if (frameGrabber != null) {
                frameGrabber.close();
                frameGrabber.stop();
            }
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

    private void initFrame(String title) {
        frame = new JFrame(title);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
                e.getWindow().dispose();
            }
        });
    }

    private JPanel initNavigationPanel() {
        NavigationBottomPanel panel = new NavigationBottomPanel();
        panel.setIcons("icons/ic_back.png", "icons/ic_home.png", "icons/ic_square.png");
        panel.setBackground(Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        panel.setBounds(0,
                imageHeight,
                frame.getWidth(),
                DimensionConst.NAVIGATION_PANEL_HEIGHT);
        panel.setItemClickListener(listener);
        panel.createPanel();

        return panel;
    }

    private NavigationBottomPanel.OnNavigationPanelListener listener = id -> {
        int keyCode = AdbKeyCode.KEYCODE_UNKNOWN.getKeyCode();
        switch (id) {
            case 0:
                keyCode = AdbKeyCode.KEYCODE_BACK.getKeyCode();
                break;
            case 1:
                keyCode = AdbKeyCode.KEYCODE_HOME.getKeyCode();
                break;
            case 2:
                keyCode = AdbKeyCode.KEYCODE_APP_SWITCH.getKeyCode();
                break;
        }
        if (keyCode != AdbKeyCode.KEYCODE_UNKNOWN.getKeyCode()) {
            chimpDevice.press(String.valueOf(keyCode), TouchPressType.DOWN_AND_UP);
        }
    };

    private void initMouseListener() {
        FrameMouseListener listener = new FrameMouseListener(this);
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    private void initKeyboardListener() {
        KeyboardListener listener = new KeyboardListener(this);
        frame.addKeyListener(listener);
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

    public int getFrameHeight() {
        return getHeight() - DimensionConst.NAVIGATION_PANEL_HEIGHT;
    }
}
