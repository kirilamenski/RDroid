package com.ansgar.rdroidpc.ui.frames.presenters;

import com.android.chimpchat.core.TouchPressType;
import com.ansgar.rdroidpc.constants.AdbKeyCode;
import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.entities.ScreenRecordOptions;
import com.ansgar.rdroidpc.enums.MenuItemsEnum;
import com.ansgar.rdroidpc.enums.OrientationEnum;
import com.ansgar.rdroidpc.listeners.*;
import com.ansgar.rdroidpc.listeners.impl.DeviceActionsImpl;
import com.ansgar.rdroidpc.ui.components.ButtonsPanel;
import com.ansgar.rdroidpc.ui.components.FileChooser;
import com.ansgar.rdroidpc.ui.components.OptionDialog;
import com.ansgar.rdroidpc.ui.components.SpinnerDialog;
import com.ansgar.rdroidpc.ui.frames.ScreenRecordOptionsFrame;
import com.ansgar.rdroidpc.ui.frames.VideoFrame;
import com.ansgar.rdroidpc.ui.frames.views.VideoFrameView;
import com.ansgar.rdroidpc.utils.DateUtil;
import com.ansgar.rdroidpc.utils.OrientationUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class VideoFramePresenter extends BasePresenter implements OnFileChooserListener,
        OnDeviceOrientationListener, OnScreenRecordOptionsListener {

    private VideoFrameView view;
    private DeviceActions deviceActions;
    private OrientationUtil orientationUtil;

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
                openScreenRecordOptions();
                break;
            case 3:
                view.press(String.valueOf(AdbKeyCode.KEYCODE_MENU.getKeyCode()), TouchPressType.DOWN_AND_UP);
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
        deviceActions.screenCapture(DateUtil.getCurrentDate() + ".png", path);
    }

    @Override
    public void onOrientationChanged(OrientationEnum orientation) {
        view.changeOrientation(orientation);
    }

    @Override
    public void onOptionsSelected(ScreenRecordOptions options) {
        deviceActions.screenRecord(options, DateUtil.getCurrentDate() + ".mp4");
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
     * By default it is reconnect to monkey runner but if shared screen from android is empty
     * then reconnect adb screenrecord.
     */
    private void reconnect() {
        SpinnerDialog dialog = new SpinnerDialog(view.getParentComponent()) {
            @Override
            protected Void doInBackground() {
                publish();
                view.disposeDevice();
                view.stopThread();
                view.initChimpDevice();
                if (view.isScreenEmpty()) {
                    view.stopFrameGrabber();
                    if (orientationUtil != null) orientationUtil.stop();
                    view.startNewThread();
                }
                return null;
            }
        };
        dialog.execute();
    }

    /**
     * Todo add button to right panel with multiple actions list such as reboot, turn off, wake up, and other actions with device
     */
    private void confirmReboot() {
        int value = new OptionDialog()
                .setDialogTitle(StringConst.ASK_REBOOT)
                .setMainTitle("")
                .showDialog(view.getChildComponent(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (value == 0) {
            view.onCloseFrame();
            deviceActions.restart();
        }
    }

}
