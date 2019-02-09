package com.ansgar.rdroidpc.ui.frames;

import com.android.chimpchat.adb.AdbBackend;
import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.TouchPressType;
import com.ansgar.rdroidpc.constants.*;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.enums.OrientationEnum;
import com.ansgar.rdroidpc.enums.OsEnum;
import com.ansgar.rdroidpc.listeners.OnDeviceOrientationListener;
import com.ansgar.rdroidpc.ui.components.ButtonsPanel;
import com.ansgar.rdroidpc.listeners.FrameMouseListener;
import com.ansgar.rdroidpc.listeners.KeyboardListener;
import com.ansgar.rdroidpc.listeners.OnVideoFrameListener;
import com.ansgar.rdroidpc.utils.*;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.ansgar.rdroidpc.constants.DimensionConst.DEFAULT_WIDTH;

public class VideoFrame extends BasePanel implements OnDeviceOrientationListener {

    private Thread thread;
    private FrameGrabber frameGrabber;
    private BufferedImage currentImage;
    private Device device;
    private IChimpDevice chimpDevice;
    private CommandExecutor commandExecutor;
    private AtomicBoolean isThreadRunning;
    private OnVideoFrameListener onVideoFrameListener;
    private OrientationUtil orientationUtil;
    private OrientationEnum currentOrientation;
    private ButtonsPanel panel;

    private int imageWidth, imageHeight, x, y;

    public VideoFrame(Device device, AdbBackend adbBackend, Rectangle rectangle) {
        super(rectangle, String.format("%s(%dx%d)", device.getDeviceName(), device.getWidth(), device.getHeight()));
        this.device = device;
        this.chimpDevice = adbBackend.waitForConnection(2147483647L, device.getDeviceId());
        this.isThreadRunning = new AtomicBoolean();
        this.orientationUtil = new OrientationUtil(device, this);
        new FileUploader(this, device);

        setLayout(null);
        changeOrientation(OrientationEnum.PORTRAIT);
        initMouseListener();
        initKeyboardListener();
    }

    public void startNewThread(String command) {
        if (thread != null) return;
        thread = new Thread(() -> start(command));
        thread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(currentImage, x, 0, imageWidth, imageHeight, this);
            g2d.dispose();
        }
    }

    @Override
    protected void onCloseApp() {
        super.onCloseApp();
        stop(true);
    }

    @Override
    public void onOrientationChanged(OrientationEnum orientation) {
        changeOrientation(orientation);
    }

    public void stop(boolean closeFrame) {
        if (orientationUtil != null) orientationUtil.stop();
        if (commandExecutor != null) commandExecutor.destroy();
        if (chimpDevice != null) chimpDevice.dispose();
        stopThread();
        stopGrabber();
        if (closeFrame) stopFrame();
    }

    private void start(String command) {
        System.out.println(command);
        commandExecutor = new CommandExecutor();
        isThreadRunning.set(true);
        orientationUtil.start(5000, 5000);

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

    private void addNavigationPanel() {
        if (panel != null) remove(panel);
        panel = new ButtonsPanel();
        panel.setIcons("icons/ic_back.png", "icons/ic_home.png", "icons/ic_square.png");
        panel.setBounds(0,
                imageHeight,
                frame.getWidth(),
                DimensionConst.NAVIGATION_PANEL_HEIGHT);
        panel.setItemClickListener(listener);
        panel.createPanel();

        add(panel);
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

    private void initMouseListener() {
        FrameMouseListener listener = new FrameMouseListener(this);
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    private void initKeyboardListener() {
        KeyboardListener listener = new KeyboardListener(this);
        frame.addKeyListener(listener);
    }

    private void changeOrientation(OrientationEnum orientationEnum) {
        Rectangle rectangle = new Rectangle(
                getRectangle().x,
                getRectangle().y,
                DEFAULT_WIDTH / 2,
                imageHeight + DimensionConst.NAVIGATION_PANEL_HEIGHT + OsEnum.Companion.getOsType().getHeightOffset()
        );
        if (orientationEnum == OrientationEnum.PORTRAIT) {
            initPortraitOrientationSize();
            rectangle.width = imageWidth - (imageWidth + x + 20);
        } else {
            initLandscapeOrientationSize();
            rectangle.width = imageWidth;
        }
        updateWindowSize(rectangle);
        currentOrientation = orientationEnum;
    }

    /**
     * Uses a static video frame size equal to 70% of the height of the PC screen to avoid input issues
     * because {@link KeyboardListener} uses resized value from {@link DimensionUtils}
     * which related of device screen size and video frame size.
     */
    private void initPortraitOrientationSize() {
        int screenHeight = SharedValues.get(StringConst.SHARED_VAL_SCREEN_HEIGHT, 0);
        imageHeight = (int) (screenHeight * 0.7f);
        imageWidth = (int) (imageHeight * 3.2f * DimensionConst.SCREEN_RATIO);

        x = -(imageWidth / 3 + 10);
    }

    private void initLandscapeOrientationSize() {
        int screenHeight = SharedValues.get(StringConst.SHARED_VAL_SCREEN_HEIGHT, 0);
        imageHeight = (int) (screenHeight * 0.7f);
        imageWidth = (int) (imageHeight / DimensionConst.SCREEN_RATIO);

        x = 0;
    }

    private void updateWindowSize(@NotNull Rectangle rectangle) {
        frame.setBounds(rectangle);
        addNavigationPanel();
        frame.revalidate();
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

    public OrientationEnum getCurrentOrientation() {
        return currentOrientation;
    }

    public void setOnVideoFrameListener(OnVideoFrameListener onVideoFrameListener) {
        this.onVideoFrameListener = onVideoFrameListener;
    }
}
