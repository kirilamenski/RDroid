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
import com.ansgar.rdroidpc.listeners.impl.VideoFrameMenuListenerImpl;
import com.ansgar.rdroidpc.ui.components.ButtonsPanel;
import com.ansgar.rdroidpc.ui.components.menu.MenuBar;
import com.ansgar.rdroidpc.ui.components.videocomponent.VideoComponent;
import com.ansgar.rdroidpc.ui.frames.presenters.BasePresenter;
import com.ansgar.rdroidpc.ui.frames.presenters.VideoFramePresenter;
import com.ansgar.rdroidpc.ui.frames.views.VideoFrameView;
import com.ansgar.rdroidpc.utils.*;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.ansgar.rdroidpc.constants.DimensionConst.DEFAULT_HEIGHT;
import static com.ansgar.rdroidpc.constants.DimensionConst.DEFAULT_WIDTH;

public class VideoFrame extends BasePanel implements VideoFrameView, OnDeviceInputListener {

    private AdbBackend adbBackend;
    private Thread thread;
    private FFmpegFrameGrabber frameGrabber;
    private BufferedImage currentImage;
    private Device device;
    private IChimpDevice chimpDevice;
    private AtomicBoolean isThreadRunning;
    private OnVideoFrameListener onVideoFrameListener;
    private OrientationEnum currentOrientation;
    private ButtonsPanel rightPanel;
    private VideoFramePresenter presenter;
    private String adbStreamCommand;
    private MenuBar menuBar;
    private VideoComponent videoComponent;

    private int imageWidth, imageHeight, imageCoordinateX;
    private float deviceScreenRatio;

    public VideoFrame(Device device, AdbBackend adbBackend, Rectangle rectangle) {
        super(rectangle, String.format("%s(%dx%d)", device.getDeviceName(), device.getWidth(), device.getHeight()));
        this.adbBackend = adbBackend;
        this.device = device;
        this.isThreadRunning = new AtomicBoolean();
        this.adbStreamCommand = StringUtils.getScreenRecordCommand(device, 45);
        this.deviceScreenRatio = device.getWidth() * 1f / device.getHeight();
        presenter.iniDeviceAction(device.getDeviceId());
        new FileUploader(this, device.getDeviceId());
        initChimpDevice();
        setLayout(null);
        changeOrientation(OrientationEnum.PORTRAIT);
    }

    @Override
    public void startNewThread() {
        if (thread != null) return;
        thread = new Thread(() -> start(adbStreamCommand));
        thread.start();
    }

    @Override
    public void start(String command) {
        isThreadRunning.set(true);
        presenter.startCheckOrientationThread(device.getDeviceId(), 5000, 5000);

        try {
            InputStream inputStream = presenter.getInputScream(command);
            frameGrabber = new FFmpegFrameGrabber(inputStream);
            frameGrabber.start();

            Java2DFrameConverter converter = new Java2DFrameConverter();
            while (isThreadRunning.get()) {
                currentImage = converter.getBufferedImage(frameGrabber.grab());
                if (currentImage != null) {
                    videoComponent.start(currentImage);
                } else {
                    presenter.reconnect();
                    break;
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            currentImage = null;
        }
    }

    @Override
    public void stop(boolean closeFrame) {
        disposeChimpDevice();
        stopThread();
        stopFrameGrabber();
        if (closeFrame) stopFrame();
    }

    @Override
    public void disposeChimpDevice() {
        if (chimpDevice != null) {
            chimpDevice.dispose();
            chimpDevice = null;
        }
    }

    @Override
    public void stopFrameGrabber() {
        try {
            if (frameGrabber != null && frameGrabber.hasVideo()) {
                frameGrabber.stop();
            }
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopThread() {
        if (thread != null) {
            isThreadRunning.set(false);
            try {
                thread.join(1000);
                thread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stopFrame() {
        closeFrame();
        if (onVideoFrameListener != null) onVideoFrameListener.onDeviceConnectionClosed(device);
    }

    @Override
    public void initChimpDevice() {
        this.chimpDevice = adbBackend.waitForConnection(2147483647L, device.getDeviceId());
    }

    @Override
    public void onCloseFrame() {
        stop(true);
    }

    @Override
    public void press(String value, TouchPressType type) {
        chimpDevice.press(value, TouchPressType.DOWN_AND_UP);
    }

    @Override
    public void type(String type) {
        chimpDevice.type(type);
    }

    @Override
    public void touch(int x, int y, TouchPressType type) {
        chimpDevice.touch(x, y, type);
    }

    @Override
    public void createPresenter() {
        presenter = new VideoFramePresenter(this, this);
    }

    @Nullable
    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void changeOrientation(OrientationEnum orientationEnum) {
        Rectangle rectangle = new Rectangle(getRectangle().x, getRectangle().y,
                DEFAULT_WIDTH / 2, DEFAULT_HEIGHT / 2);
        if (orientationEnum == OrientationEnum.PORTRAIT) {
            initPortraitOrientationSize();
            rectangle.width = (int) Math.ceil(imageHeight * deviceScreenRatio + getRightOffset());
        } else {
            initLandscapeOrientationSize();
            rectangle.width = imageWidth;
        }
        rectangle.height = imageHeight;
        currentOrientation = orientationEnum;
        updateWindowSize(rectangle);
    }

    /**
     * Uses a static video frame size equal to 70% of the height of the PC screen to avoid input issues
     * because {@link KeyboardListener} uses resized value from {@link DimensionUtils}
     * which related of device screen size and video frame size.
     */
    private void initPortraitOrientationSize() {
        int screenHeight = SharedValues.get(StringConst.SHARED_VAL_SCREEN_HEIGHT, 0);
        imageHeight = (int) (screenHeight * 0.7f) + DimensionConst.MENU_HEIGHT;
        imageWidth = (int) (imageHeight * getWidthOffset() * deviceScreenRatio);
        imageCoordinateX = -(imageWidth / 3 + getCoordinateOffset(getWidthOffset()));
    }

    private void initLandscapeOrientationSize() {
        int screenHeight = SharedValues.get(StringConst.SHARED_VAL_SCREEN_HEIGHT, 0);
        imageHeight = (int) (screenHeight * 0.7f) + DimensionConst.MENU_HEIGHT;
        imageWidth = (int) (imageHeight / deviceScreenRatio);
        imageCoordinateX = 0;
    }

    private void updateWindowSize(@NotNull Rectangle rectangle) {
        frame.setBounds(new Rectangle(
                rectangle.x,
                rectangle.y,
                rectangle.width + DimensionConst.RIGHT_ACTION_PANEL_WIDTH,
                rectangle.height + DimensionConst.MENU_HEIGHT + OsEnum.Companion.getOsType().getHeightOffset()
        ));
        addMenu();
        addVideo(rectangle.width, rectangle.height);
        addRightPanel();
        repaint();
    }

    private void addVideo(int width, int height) {
        Rectangle bounds = new Rectangle(0, menuBar.getHeight(), width, height);
        if (videoComponent == null) {
            videoComponent = new VideoComponent(device, this);
            add(videoComponent);
        }
        videoComponent.setBounds(bounds);
        videoComponent.setCoordinateX(imageCoordinateX);
        videoComponent.setImageWidth(imageWidth);
        videoComponent.setImageHeight(imageHeight);
        videoComponent.setCurrentOrientation(currentOrientation);
    }

    private void addMenu() {
        Rectangle bounds = new Rectangle(0, 0, frame.getWidth(), DimensionConst.MENU_HEIGHT);
        if (menuBar == null) {
            menuBar = new MenuBar();
            menuBar.setListener(new VideoFrameMenuListenerImpl(this));
            add(menuBar.getMenuBar(StringConst.Companion.getVideoMenuItems(), bounds));
        } else {
            menuBar.setBounds(bounds);
        }
    }

    private void addRightPanel() {
        Rectangle bounds = new Rectangle(videoComponent.getWidth(), menuBar.getHeight(),
                DimensionConst.RIGHT_ACTION_PANEL_WIDTH, imageHeight);
        if (rightPanel == null) {
            rightPanel = new ButtonsPanel();
            rightPanel.setBorder(new MatteBorder(1, 1, 0, 0, Color.BLACK));
            rightPanel.setIcons(StringConst.Companion.getNavigationPanelIcons());
            rightPanel.setToolTips(StringConst.Companion.getNavigationPanelTooltips());
            rightPanel.setState(ButtonsPanelStateEnum.VERTICAL);
            rightPanel.setIconSize(42, 42);
            rightPanel.setBounds(bounds);
            rightPanel.setItemClickListener(presenter.rightActionsListener);
            rightPanel.createPanel();
            add(rightPanel);
        } else {
            rightPanel.setBounds(bounds);
        }
    }

    /**
     * Different screen size has different offset and coordination to display image. Has been implemented these functions
     * to make it same for different devices.
     * TODO need to be refactored. In better case try to find formula of relations or to make some util class which will
     * return hardcoded values for different screens.
     */
    private int getRightOffset() {
        if (deviceScreenRatio != DimensionConst.SCREEN_RATIO) {
            return (int) Math.ceil(deviceScreenRatio * DimensionConst.SCREEN_RATIO * 100) + 10;
        }
        return 5;
    }

    private float getWidthOffset() {
        if (deviceScreenRatio != DimensionConst.SCREEN_RATIO) {
            return (float) Math.round(DimensionUtils
                    .round(device.getHeight() * 1f / device.getWidth() / deviceScreenRatio, 10));
        }
        return 3.2f;
    }

    private int getCoordinateOffset(float widthOffset) {
        if (deviceScreenRatio != DimensionConst.SCREEN_RATIO) {
            return (int) (10 * widthOffset / deviceScreenRatio) - 5;
        }
        return 10;
    }

    @Override
    public Device getDevice() {
        return device;
    }

    public OrientationEnum getCurrentOrientation() {
        return currentOrientation;
    }

    @Override
    public boolean isScreenEmpty() {
        return currentImage == null;
    }

    public void setOnVideoFrameListener(OnVideoFrameListener onVideoFrameListener) {
        this.onVideoFrameListener = onVideoFrameListener;
    }

}
