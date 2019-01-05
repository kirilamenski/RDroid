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
import com.ansgar.rdroidpc.ui.components.ButtonsPanel;
import com.ansgar.rdroidpc.listeners.FrameMouseListener;
import com.ansgar.rdroidpc.listeners.KeyboardListener;
import com.ansgar.rdroidpc.listeners.OnVideoFrameListener;
import com.ansgar.rdroidpc.utils.ToolkitUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public class VideoFrame extends JPanel {
    private JFrame frame;
    private Thread thread;
    private FrameGrabber frameGrabber;
    private BufferedImage currentImage;
    private Device device;
    private IChimpDevice chimpDevice;
    private CommandExecutor commandExecutor;
    private AtomicBoolean isThreadRunning;
    private OnVideoFrameListener onVideoFrameListener;

    private int imageWidth, imageHeight;
    private int xCoord;

    private boolean isWindowUpdated;

    public VideoFrame(Device device, AdbBackend adbBackend) {
        this.device = device;
        this.chimpDevice = adbBackend.waitForConnection(2147483647L, device.getDeviceId());
        this.isThreadRunning = new AtomicBoolean();

        setLayout(null);
        initDimension();
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
        isThreadRunning.set(true);

        try {
            InputStream inputStream = commandExecutor.getInputStream(command);
            frameGrabber = new FFmpegFrameGrabber(inputStream);
            frameGrabber.start();

            Java2DFrameConverter converter = new Java2DFrameConverter();
            while (isThreadRunning.get() && frameGrabber != null && frameGrabber.grab() != null) {
                if (isThreadRunning.get()) {
                    currentImage = converter.getBufferedImage(frameGrabber.grab());
                    repaint();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            int x = -(imageWidth / 3 + 10);
            g2d.drawImage(currentImage, x, 0, imageWidth, imageHeight, this);
            g2d.dispose();

            if (!isWindowUpdated) updateWindowSize(x);
        }
    }

    public void stop(boolean closeFrame) {
        if (commandExecutor != null) commandExecutor.destroy();
        if (chimpDevice != null) chimpDevice.dispose();
        stopThread();
        stopGrabber();
        restartServer();
        if (closeFrame) stopFrame();
    }

    private void updateWindowSize(int x) {
        frame.setBounds(xCoord, 0,
                imageWidth - (imageWidth + x + 20),
                imageHeight + DimensionConst.NAVIGATION_PANEL_HEIGHT + 48);
        add(initNavigationPanel());
        frame.revalidate();
        isWindowUpdated = true;
    }

    private void stopGrabber() {
        try {
            if (frameGrabber != null) {
                frameGrabber.close();
            }
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }

    private void stopThread() {
        if (thread != null && thread.isAlive()) {
            isThreadRunning.set(false);
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopFrame() {
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose();
            if (onVideoFrameListener != null) onVideoFrameListener.onClosed(device);
        }
    }

    private void restartServer() {
        commandExecutor.execute(AdbCommandEnum.KILL_SERVER);
        commandExecutor.execute(AdbCommandEnum.START_SERVER);
    }

    private void initFrame(String title) {
        frame = new JFrame(title);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        frame.setResizable(false);
        frame.setBounds(xCoord, 0,
                DimensionConst.DEFAULT_WIDTH / 2,
                (int) (DimensionConst.DEFAULT_WIDTH / DimensionConst.SCREEN_RATIO / 2));
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop(true);
                e.getWindow().dispose();
            }
        });
    }

    private JPanel initNavigationPanel() {
        ButtonsPanel panel = new ButtonsPanel();
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

    private ButtonsPanel.OnButtonPanelListener listener = id -> {
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

    /**
     * Uses a static video frame size equal to 70% of the height of the PC screen to avoid input issues
     * because {@link KeyboardListener} uses resized value from {@link com.ansgar.rdroidpc.utils.CoordinatesConverter}
     * which related of device screen size and video frame size.
     */
    private void initDimension() {
        int screenWidth = ToolkitUtils.getWindowSize().width;
        xCoord = screenWidth / 2 - DimensionConst.DEFAULT_WIDTH / 2;
        imageHeight = (int) (screenWidth * 0.7f * DimensionConst.SCREEN_RATIO);
        imageWidth = (int) (imageHeight * 3.2f * DimensionConst.SCREEN_RATIO);
    }

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

    public void setOnVideoFrameListener(OnVideoFrameListener onVideoFrameListener) {
        this.onVideoFrameListener = onVideoFrameListener;
    }
}
