package com.ansgar.rdroidpc.ui.frames;

import com.android.chimpchat.adb.AdbBackend;
import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.TouchPressType;
import com.ansgar.rdroidpc.constants.*;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.enums.ButtonsPanelStateEnum;
import com.ansgar.rdroidpc.enums.OrientationEnum;
import com.ansgar.rdroidpc.enums.OsEnum;
import com.ansgar.rdroidpc.listeners.*;
import com.ansgar.rdroidpc.listeners.impl.DeviceActionsImpl;
import com.ansgar.rdroidpc.ui.components.ButtonsPanel;
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
    private AtomicBoolean isThreadRunning;
    private OnVideoFrameListener onVideoFrameListener;
    private OrientationUtil orientationUtil;
    private OrientationEnum currentOrientation;
    private ButtonsPanel bottomPanel, rightPanel;
    private DeviceActions deviceActions;

    private int imageWidth, imageHeight, imageCoordinateX;

    public VideoFrame(Device device, AdbBackend adbBackend, Rectangle rectangle) {
        super(rectangle, String.format("%s(%dx%d)", device.getDeviceName(), device.getWidth(), device.getHeight()));
        this.device = device;
        this.chimpDevice = adbBackend.waitForConnection(2147483647L, device.getDeviceId());
        this.isThreadRunning = new AtomicBoolean();
        this.orientationUtil = new OrientationUtil(device, this);
        this.deviceActions = new DeviceActionsImpl(this);
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
            g2d.drawImage(currentImage, imageCoordinateX, 0, imageWidth, imageHeight, this);
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
        if (deviceActions != null) {
            deviceActions.disableAccelerometer(1);
            deviceActions.destroy();
        }
        if (chimpDevice != null) chimpDevice.dispose();
        stopThread();
        stopGrabber();
        if (closeFrame) stopFrame();
    }

    private void start(String command) {
        isThreadRunning.set(true);
        orientationUtil.start(5000, 5000);

        try {
            InputStream inputStream = deviceActions.getInputStream(command);
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

    private void addBottomPanel() {
        if (bottomPanel != null) remove(bottomPanel);
        bottomPanel = new ButtonsPanel();
        bottomPanel.setIcons("icons/ic_back.png", "icons/ic_home.png", "icons/ic_square.png");
        bottomPanel.setBounds(0, imageHeight, getFrameWidth(), DimensionConst.BOTTOM_ACTION_PANEL_HEIGHT);
        bottomPanel.setItemClickListener(bottomActionsListener);
        bottomPanel.createPanel();

        add(bottomPanel);
    }

    private void addRightPanel() {
        if (rightPanel != null) remove(rightPanel);
        rightPanel = new ButtonsPanel();
        rightPanel.setIcons("icons/ic_rotate_64.png", "icons/ic_screen_capture_64.png", "icons/ic_screen_record_64.png");
        rightPanel.setState(ButtonsPanelStateEnum.VERTICAL);
        rightPanel.setIconSize(42, 42);
        rightPanel.setBounds(getFrameWidth(), 0, DimensionConst.RIGHT_ACTION_PANEL_WIDTH, frame.getHeight());
        rightPanel.setItemClickListener(rightActionsListener);
        rightPanel.createPanel();

        add(rightPanel);
    }

    private ButtonsPanel.OnButtonPanelListener bottomActionsListener = id -> {
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

    private ButtonsPanel.OnButtonPanelListener rightActionsListener = id -> {
        switch (id) {
            case 0:
                deviceActions.changeOrientation(currentOrientation == OrientationEnum.PORTRAIT ? 1 : 0);
                break;
            case 1:
                deviceActions.screenCapture();
                break;
            case 2:
                break;
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
                imageHeight + DimensionConst.BOTTOM_ACTION_PANEL_HEIGHT + OsEnum.Companion.getOsType().getHeightOffset()
        );
        if (orientationEnum == OrientationEnum.PORTRAIT) {
            initPortraitOrientationSize();
            rectangle.width = imageWidth - (imageWidth + imageCoordinateX + 20) + DimensionConst.RIGHT_ACTION_PANEL_WIDTH;
        } else {
            initLandscapeOrientationSize();
            rectangle.width = imageWidth + DimensionConst.RIGHT_ACTION_PANEL_WIDTH;
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
        imageCoordinateX = -(imageWidth / 3 + 10);
    }

    private void initLandscapeOrientationSize() {
        int screenHeight = SharedValues.get(StringConst.SHARED_VAL_SCREEN_HEIGHT, 0);
        imageHeight = (int) (screenHeight * 0.7f);
        imageWidth = (int) (imageHeight / DimensionConst.SCREEN_RATIO);
        imageCoordinateX = 0;
    }

    private void updateWindowSize(@NotNull Rectangle rectangle) {
        frame.setBounds(rectangle);
        addBottomPanel();
        addRightPanel();
//        frame.revalidate();
        repaint();
    }

    public Device getDevice() {
        return device;
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
        return getHeight() - DimensionConst.BOTTOM_ACTION_PANEL_HEIGHT;
    }

    public int getFrameWidth() {
        return getWidth() - DimensionConst.RIGHT_ACTION_PANEL_WIDTH;
    }

    public OrientationEnum getCurrentOrientation() {
        return currentOrientation;
    }

    public void setOnVideoFrameListener(OnVideoFrameListener onVideoFrameListener) {
        this.onVideoFrameListener = onVideoFrameListener;
    }

}
