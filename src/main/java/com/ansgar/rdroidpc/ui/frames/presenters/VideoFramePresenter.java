package com.ansgar.rdroidpc.ui.frames.presenters;

import com.android.chimpchat.core.TouchPressType;
import com.ansgar.filemanager.DesktopUtil;
import com.ansgar.rdroidpc.constants.AdbKeyCode;
import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.SharedValuesKey;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.entities.ScreenRecordOptions;
import com.ansgar.rdroidpc.enums.MainMenuItemsEnum;
import com.ansgar.rdroidpc.enums.OrientationEnum;
import com.ansgar.rdroidpc.enums.VideoMenuItemsEnum;
import com.ansgar.rdroidpc.listeners.*;
import com.ansgar.rdroidpc.listeners.impl.DeviceActionsImpl;
import com.ansgar.rdroidpc.ui.components.*;
import com.ansgar.rdroidpc.ui.components.DumpsysPanel;
import com.ansgar.rdroidpc.ui.frames.PackageManagerPanel;
import com.ansgar.rdroidpc.ui.frames.ScreenRecordOptionsFrame;
import com.ansgar.rdroidpc.ui.frames.VideoFrame;
import com.ansgar.rdroidpc.ui.frames.views.VideoFrameView;
import com.ansgar.rdroidpc.utils.DateUtil;
import com.ansgar.rdroidpc.commands.tasks.OrientationCommandTask;
import com.ansgar.rdroidpc.utils.SharedValues;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class VideoFramePresenter extends BasePresenter<VideoFrameView> implements OnFileChooserListener,
        OnDeviceOrientationListener, OnScreenRecordOptionsListener {

    private String deviceId;
    private OnDeviceInputListener inputListener;
    private DeviceActions deviceActions;
    private OrientationCommandTask orientationCommandTask;
    private AtomicInteger screenRecordTimeLeft;
    private SpinnerDialog spinnerDialog;

    public VideoFramePresenter(VideoFrameView view, OnDeviceInputListener listener) {
        super(view);
        this.inputListener = listener;
        VideoFrame frame = (VideoFrame) view.getChildComponent();
        view.setKeyboardListener(new KeyboardListener(frame));
    }

    public void iniDeviceAction(String deviceId) {
        this.deviceId = deviceId;
        this.deviceActions = new DeviceActionsImpl(deviceId);
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
                inputListener.press(String.valueOf(AdbKeyCode.KEYCODE_BACK.getKeyCode()), TouchPressType.DOWN_AND_UP);
                break;
            case 6:
                inputListener.press(String.valueOf(AdbKeyCode.KEYCODE_HOME.getKeyCode()), TouchPressType.DOWN_AND_UP);
                break;
            case 7:
                inputListener.press(String.valueOf(AdbKeyCode.KEYCODE_APP_SWITCH.getKeyCode()),
                        TouchPressType.DOWN_AND_UP);
                break;
        }
    };

    public InputStream getInputScream(String command) throws IOException {
        return deviceActions.getInputStream(command);
    }

    public void startCheckOrientationThread(String deviceId, int delay, int period) {
        orientationCommandTask = new OrientationCommandTask();
        orientationCommandTask.setDeviceId(deviceId);
        orientationCommandTask.setListener(this);
        orientationCommandTask.start(delay, period);
    }

    public void inputText(String text) {
        deviceActions.inputText(text);
    }

    @Override
    public void destroy() {
        if (orientationCommandTask != null) orientationCommandTask.stop();
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
        view.getChildComponent().add(component, 0);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                screenRecordTimeLeft.set(screenRecordTimeLeft.get() - 1);
                component.updateTitle(String.format(StringConst.SCREEN_RECORDING_L, screenRecordTimeLeft.get()));
                view.getChildComponent().repaint();
                if (screenRecordTimeLeft.get() <= 0) {
                    screenRecordTimeLeft = null;
                    timer.cancel();
                    view.getChildComponent().remove(component);
                }
            }
        }, 0, 1000);
    }

    private void openFileChooser() {
        FileChooser chooser = new FileChooser(this);
        String defaultPath = SharedValues.get(SharedValuesKey.DEFAULT_FILE_UPLOADING_FOLDER, System.getProperty("user.home"));
        chooser.open(view.getChildComponent(), JFileChooser.DIRECTORIES_ONLY, defaultPath);
    }

    private void openScreenRecordOptions() {
        Rectangle rectangle = new Rectangle(
                view.getRectangle().x,
                view.getRectangle().y,
                DimensionConst.SETTINGS_PANEL_WIDTH,
                DimensionConst.SETTINGS_PANEL_HEIGHT
        );
        ScreenRecordOptionsFrame settingsFrame = new ScreenRecordOptionsFrame(view.getChildComponent(), rectangle,
                MainMenuItemsEnum.SETTINGS.getValue());
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
        if (deviceActions.isDevicesConnected(deviceId)) {
            spinnerDialog = new SpinnerDialog(view.getChildComponent()) {
                @Override
                public void doInBack() {
                    view.disposeChimpDevice();
                    view.initChimpDevice();
                    if (view.isScreenEmpty()) {
                        if (orientationCommandTask != null) orientationCommandTask.stop();
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

    public void openPanel(VideoMenuItemsEnum videoItem, String deviceId, Rectangle rectangle) {
        switch (videoItem) {
            case PACKAGE_MANAGER:
                new PackageManagerPanel(deviceId, rectangle, StringConst.PACKAGE_MANAGER);
                break;
            case PERFORMANCE_MANAGER:
                new DumpsysPanel(deviceId, rectangle, StringConst.DUMP_SYS_MANAGER);
                break;
            default:
                break;
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
//            view.getChimpDevice().reboot("None");
        }
    }

    public OverlayComponent getOverlayComponent() {
        OverlayComponent component = new OverlayComponent();
        component.setBounds(new Rectangle(0, view.getParentComponent().getHeight() - 85,
                view.getParentComponent().getWidth(), 30));
        component.setImageSrc("icons/ic_screen_record_64.png");
        component.setImageWidth(30);
        component.setImageHeight(30);
        component.createComponent();
        return component;
    }

}
