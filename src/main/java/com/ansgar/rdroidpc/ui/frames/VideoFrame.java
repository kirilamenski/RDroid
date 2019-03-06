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
import com.ansgar.rdroidpc.ui.components.ButtonsPanel;
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
    private FrameGrabber frameGrabber;
    private BufferedImage currentImage;
    private Device device;
    private IChimpDevice chimpDevice;
    private AtomicBoolean isThreadRunning;
    private OnVideoFrameListener onVideoFrameListener;
    private OrientationEnum currentOrientation;
    private ButtonsPanel rightPanel;
    private VideoFramePresenter presenter;
    private String adbStreamCommand;

    private int imageWidth, imageHeight, imageCoordinateX;

    public VideoFrame(Device device, AdbBackend adbBackend, Rectangle rectangle) {
        super(rectangle, String.format("%s(%dx%d)", device.getDeviceName(), device.getWidth(), device.getHeight()));
        this.adbBackend = adbBackend;
        this.device = device;
        this.isThreadRunning = new AtomicBoolean();
        this.adbStreamCommand = StringUtils.getScreenRecordCommand(device, 45);
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(currentImage, imageCoordinateX, 0, imageWidth, imageHeight, this);
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
            rectangle.width = imageWidth - (imageWidth + imageCoordinateX + 30) + DimensionConst.RIGHT_ACTION_PANEL_WIDTH;
        } else {
            initLandscapeOrientationSize();
            rectangle.width = imageWidth + DimensionConst.RIGHT_ACTION_PANEL_WIDTH;
        }
        rectangle.height = imageHeight + OsEnum.Companion.getOsType().getHeightOffset();
        updateWindowSize(rectangle);
        currentOrientation = orientationEnum;
    }

    @Override
    public void stop(boolean closeFrame) {
        if (chimpDevice != null) chimpDevice.dispose();
        stoStreaming();
        stopGrabber();
        if (closeFrame) stopFrame();
    }

    private void start(String command) {
        isThreadRunning.set(true);
        presenter.startCheckOrientationThread(device, 5000, 5000);

        try {
            InputStream inputStream = presenter.getInputScream(command);
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
            if (frameGrabber != null && currentImage != null) {
                frameGrabber.close();
            }
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }

    private void stoStreaming() {
        if (thread != null && thread.isAlive()) {
            isThreadRunning.set(false);
            try {
                thread.join(1000);
                thread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopFrame() {
        closeFrame();
        if (onVideoFrameListener != null) onVideoFrameListener.onDeviceConnectionClosed(device);
    }

    private void addRightPanel() {
        if (rightPanel != null) remove(rightPanel);
        rightPanel = new ButtonsPanel();
        rightPanel.setBorder(new MatteBorder(1, 1, 0, 0, Color.BLACK));
        rightPanel.setIcons(StringConst.Companion.getNavigationPanelIcons());
        rightPanel.setToolTips(StringConst.Companion.getNavigationPanelTooltips());
        rightPanel.setState(ButtonsPanelStateEnum.VERTICAL);
        rightPanel.setIconSize(42, 42);
        rightPanel.setBounds(
                frame.getWidth() - DimensionConst.RIGHT_ACTION_PANEL_WIDTH,
                0,
                DimensionConst.RIGHT_ACTION_PANEL_WIDTH,
                imageHeight
        );
        rightPanel.setItemClickListener(presenter.rightActionsListener);
        rightPanel.createPanel();

        add(rightPanel);
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
        addRightPanel();
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
        return getHeight();
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
