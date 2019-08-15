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

public class VideoFrame extends BasePanel implements VideoFrameView {

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

    private int imageWidth, imageHeight, imageCoordinateX;
    private float deviceScreenRatio;

    public VideoFrame(Device device, AdbBackend adbBackend, Rectangle rectangle) {
        super(rectangle, String.format("%s(%dx%d)", device.getDeviceName(), device.getWidth(), device.getHeight()));
        this.adbBackend = adbBackend;
        this.device = device;
        this.isThreadRunning = new AtomicBoolean();
        this.adbStreamCommand = StringUtils.getScreenRecordCommand(device, 45);
        this.deviceScreenRatio = device.getWidth() * 1f / device.getHeight();
        new FileUploader(this, device);
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
            while (isThreadRunning.get() && frameGrabber != null) {
                if (isThreadRunning.get()) {
                    currentImage = converter.getBufferedImage(frameGrabber.grab());
                    if (currentImage == null) {
                        presenter.reconnect();
                        break;
                    } else {
                        repaint();
                    }
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(currentImage, imageCoordinateX, DimensionConst.MENU_HEIGHT, imageWidth, imageHeight, this);
            g2d.dispose();
        }
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
    public void createPresenter() {
        presenter = new VideoFramePresenter(this);
    }

    @Nullable
    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void changeOrientation(OrientationEnum orientationEnum) {
        Rectangle rectangle = new Rectangle(
                getRectangle().x,
                getRectangle().y,
                DEFAULT_WIDTH / 2 + 10,
                DEFAULT_HEIGHT / 2
        );
        if (orientationEnum == OrientationEnum.PORTRAIT) {
            initPortraitOrientationSize();
            rectangle.width = (int) Math.ceil(imageHeight * deviceScreenRatio + getRightOffset()
                    + DimensionConst.RIGHT_ACTION_PANEL_WIDTH);
        } else {
            initLandscapeOrientationSize();
            rectangle.width = imageWidth + DimensionConst.RIGHT_ACTION_PANEL_WIDTH;
        }
        rectangle.height = imageHeight + OsEnum.Companion.getOsType().getHeightOffset() + DimensionConst.MENU_HEIGHT;
        updateWindowSize(rectangle);
        currentOrientation = orientationEnum;
    }

    private void createMenu() {
        menuBar = new MenuBar();
        menuBar.setListener(new VideoFrameMenuListenerImpl(this));
        add(menuBar.getMenuBar(
                StringConst.Companion.getVideoMenuItems(),
                new Rectangle(0, 0,
                        frame.getWidth() - DimensionConst.RIGHT_ACTION_PANEL_WIDTH, DimensionConst.MENU_HEIGHT)
        ));
        updateUI();
    }

    private void addRightPanel() {
        if (rightPanel != null) remove(rightPanel);
        rightPanel = new ButtonsPanel.ButtonsPanelBuilder()
                .setBorder(new MatteBorder(1, 1, 0, 0, Color.BLACK))
                .setIcons(StringConst.Companion.getNavigationPanelIcons())
                .setToolTips(StringConst.Companion.getNavigationPanelTooltips())
                .setState(ButtonsPanelStateEnum.VERTICAL)
                .setIconSize(42, 42)
                .setBounds(
                        frame.getWidth() - DimensionConst.RIGHT_ACTION_PANEL_WIDTH,
                        0,
                        DimensionConst.RIGHT_ACTION_PANEL_WIDTH,
                        imageHeight + DimensionConst.MENU_HEIGHT
                )
                .setItemClickListener(presenter.rightActionsListener)
                .build();

        add(rightPanel);
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
        frame.setBounds(rectangle);
        createMenu();
        addRightPanel();
        repaint();
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

    @Nullable
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
        return getHeight();
    }

    public int getFrameWidth() {
        return getWidth() - DimensionConst.RIGHT_ACTION_PANEL_WIDTH;
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
