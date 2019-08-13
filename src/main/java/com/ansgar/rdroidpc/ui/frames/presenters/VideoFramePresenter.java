package com.ansgar.rdroidpc.ui.frames.presenters;

import com.android.chimpchat.core.TouchPressType;
import com.ansgar.filemanager.DesktopUtil;
import com.ansgar.rdroidpc.constants.AdbKeyCode;
import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.entities.ScreenRecordOptions;
import com.ansgar.rdroidpc.enums.MenuItemsEnum;
import com.ansgar.rdroidpc.enums.OrientationEnum;
import com.ansgar.rdroidpc.listeners.*;
import com.ansgar.rdroidpc.listeners.impl.DeviceActionsImpl;
import com.ansgar.rdroidpc.ui.components.*;
import com.ansgar.rdroidpc.ui.frames.DumpsysPanel;
import com.ansgar.rdroidpc.ui.frames.ScreenRecordOptionsFrame;
import com.ansgar.rdroidpc.ui.frames.VideoFrame;
import com.ansgar.rdroidpc.ui.frames.views.VideoFrameView;
import com.ansgar.rdroidpc.utils.DateUtil;
import com.ansgar.rdroidpc.utils.OrientationUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class VideoFramePresenter extends BasePresenter implements OnFileChooserListener,
        OnDeviceOrientationListener, OnScreenRecordOptionsListener {

    private VideoFrameView view;
    private DeviceActions deviceActions;
    private OrientationUtil orientationUtil;
    private AtomicInteger screenRecordTimeLeft;
    private SpinnerDialog spinnerDialog;

    public VideoFramePresenter(VideoFrameView view) {
        super(view);
        this.view = view;
        VideoFrame frame = (VideoFrame) view.getChildComponent();
        this.deviceActions = new DeviceActionsImpl(frame);
        view.setKeyboardListener(new KeyboardListener(frame));
        initMouseListener(frame);
    }

    public ButtonsPanel.OnButtonPanelListener rightActionsListener = id -> {
        switch (id) {
            case 0:
                int orientationEnum = view.getCurrentOrientation() == OrientationEnum.PORTRAIT ? 1 : 0;
                deviceActions.changeOrientation(orientationEnum);
                break;
            case 1:
                openFileChooser();
                break;
            case 2:
                if (screenRecordTimeLeft == null) {
                    openScreenRecordOptions();
                } else {
                    view.showMessageDialog("", StringConst.SCREEN_RECORD_ALREADY_RUNNING,
                            JOptionPane.CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 3:
                deviceActions.pressKeyCode(AdbKeyCode.KEYCODE_MENU);
                break;
            case 4:
                reconnect();
                break;
            case 5:
                view.press(String.valueOf(AdbKeyCode.KEYCODE_BACK.getKeyCode()), TouchPressType.DOWN_AND_UP);
                break;
            case 6:
                view.press(String.valueOf(AdbKeyCode.KEYCODE_HOME.getKeyCode()), TouchPressType.DOWN_AND_UP);
                break;
            case 7:
                view.press(String.valueOf(AdbKeyCode.KEYCODE_APP_SWITCH.getKeyCode()), TouchPressType.DOWN_AND_UP);
                break;
            case 8:
                new DumpsysPanel(view.getDevice().getDeviceId(),
                        new Rectangle(100, 0, 600, 400), "Dumpsys Manager");
                break;
        }
    };

    public InputStream getInputScream(String command) throws IOException {
        return deviceActions.getInputStream(command);
    }

    public void startCheckOrientationThread(Device device, int delay, int period) {
        this.orientationUtil = new OrientationUtil(device, this);
        orientationUtil.start(delay, period);
    }

    @Override
    public void destroy() {
        if (orientationUtil != null) orientationUtil.stop();
        if (deviceActions != null) {
            deviceActions.disableAccelerometer(1);
            deviceActions.destroy();
        }
    }

    @Override
    public void onPathSelected(String path) {
        deviceActions.screenCapture(DateUtil.getCurrentDate() + ".png", path,
                folder -> {
                    try {
                        DesktopUtil.openFolder(folder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void onOrientationChanged(OrientationEnum orientation) {
        view.changeOrientation(orientation);
    }

    @Override
    public void onDeviceNotFounded(Throwable throwable) {
        if (spinnerDialog != null) spinnerDialog.closeSpinner();
        view.onCloseFrame();
    }

    @Override
    public void onOptionsSelected(ScreenRecordOptions options) {
        deviceActions.screenRecord(options, DateUtil.getCurrentDate() + ".mp4",
                folder -> {
                    try {
                        DesktopUtil.openFolder(folder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        startScreenRecordTimerLeft(options.getTime());
    }

    private void startScreenRecordTimerLeft(int time) {
        if (screenRecordTimeLeft == null) screenRecordTimeLeft = new AtomicInteger();
        screenRecordTimeLeft.set(time);

        OverlayComponent component = getOverlayComponent();
        view.getChildComponent().add(component);
        view.getChildComponent().repaint();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                screenRecordTimeLeft.set(screenRecordTimeLeft.get() - 1);
                component.updateTitle(String.format(StringConst.SCREEN_RECORDING_L, screenRecordTimeLeft.get()));
                if (screenRecordTimeLeft.get() <= 0) {
                    screenRecordTimeLeft = null;
                    timer.cancel();
                    view.getChildComponent().remove(component);
                }
            }
        }, 0, 1000);
    }

    private void initMouseListener(VideoFrame frame) {
        FrameMouseListener listener = new FrameMouseListener(frame);
        frame.addMouseListener(listener);
        frame.addMouseMotionListener(listener);
    }

    private void openFileChooser() {
        FileChooser chooser = new FileChooser(this);
        chooser.open(view.getChildComponent(), JFileChooser.DIRECTORIES_ONLY, "");
    }

    private void openScreenRecordOptions() {
        Rectangle rectangle = new Rectangle(
                view.getRectangle().x,
                view.getRectangle().y,
                DimensionConst.SETTINGS_PANEL_WIDTH,
                DimensionConst.SETTINGS_PANEL_HEIGHT
        );
        ScreenRecordOptionsFrame settingsFrame = new ScreenRecordOptionsFrame(view.getChildComponent(), rectangle,
                MenuItemsEnum.SETTINGS.getValue());
        settingsFrame.setListener(this);
    }

    /**
     * Run in background to display {@link SpinnerDialog}.
     * Monkey Runner can stop working after some crash in android.
     * Adb screenrecorder is limited by ~3 h. After this connection will stop.
     * By default will reconnected to monkey runner and if displayed android screen is empty
     * then reconnect adb screenrecord.
     */
    public void reconnect() {
        if (deviceActions.isDevicesConnected(view.getDevice())) {
            spinnerDialog = new SpinnerDialog(view.getParentComponent().getBounds()) {
                @Override
                public void doInBack() {
                    view.disposeChimpDevice();
                    view.initChimpDevice();
                    if (view.isScreenEmpty()) {
                        if (orientationUtil != null) orientationUtil.stop();
                        view.stopThread();
                        view.stopFrameGrabber();
                        view.startNewThread();
                    }
                    spinnerDialog = null;
                }
            };
            spinnerDialog.execute();
        } else {
            view.onCloseFrame();
        }
    }

    /**
     * Todo add button to right panel with multiple actions list such as reboot, turn off, wake up, and other actions with device
     */
    private void confirmReboot() {
        int value = view.showMessageDialog("", StringConst.ASK_REBOOT,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (value == 0) {
            view.onCloseFrame();
            deviceActions.restart();
        }
    }

    private OverlayComponent getOverlayComponent() {
        OverlayComponent component = new OverlayComponent();
        component.setBounds(new Rectangle(0, view.getChildComponent().getHeight() - 30,
                view.getChildComponent().getWidth(), 30));
        component.setImageSrc("icons/ic_screen_record_64.png");
        component.setImageWidth(30);
        component.setImageHeight(30);
        component.createComponent();
        return component;
    }

}
