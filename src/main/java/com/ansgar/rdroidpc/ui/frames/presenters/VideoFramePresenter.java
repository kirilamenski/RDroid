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
import com.ansgar.rdroidpc.ui.frames.ScreenRecordOptionsFrame;
import com.ansgar.rdroidpc.ui.frames.VideoFrame;
import com.ansgar.rdroidpc.ui.frames.views.VideoFrameView;
import com.ansgar.rdroidpc.utils.DateUtil;
import com.ansgar.rdroidpc.utils.OrientationUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class VideoFramePresenter extends BasePresenter implements OnFileChooserListener, OnDeviceOrientationListener, OnScreenRecordOptionsListener {

    private VideoFrame frame;
    private VideoFrameView view;
    private DeviceActions deviceActions;
    private OrientationUtil orientationUtil;

    public VideoFramePresenter(VideoFrame frame, VideoFrameView view) {
        super(view);
        this.frame = frame;
        this.view = view;
        this.deviceActions = new DeviceActionsImpl(frame);

        initKeyboardListener();
        initMouseListener();
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
                confirmReboot();
                break;
        }
    };

    public ButtonsPanel.OnButtonPanelListener bottomActionsListener = id -> {
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
            view.press(String.valueOf(keyCode), TouchPressType.DOWN_AND_UP);
        }
    };

    public InputStream getInputScream(String command) throws IOException {
        return deviceActions.getInputStream(command);
    }

    public void startCheckOrientation(Device device, int delay, int period) {
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

    private void initMouseListener() {
        FrameMouseListener listener = new FrameMouseListener(frame);
        frame.addMouseListener(listener);
        frame.addMouseMotionListener(listener);
    }

    private void initKeyboardListener() {
        KeyboardListener listener = new KeyboardListener(frame);
        frame.addKeyListener(listener);
    }

    private void openFileChooser() {
        FileChooser chooser = new FileChooser(this);
        chooser.open(JFileChooser.DIRECTORIES_ONLY);
    }

    private void openScreenRecordOptions() {
        Rectangle rectangle = new Rectangle(
                view.getRectangle().x,
                view.getRectangle().y,
                DimensionConst.SETTINGS_PANEL_WIDTH,
                DimensionConst.SETTINGS_PANEL_HEIGHT
        );
        ScreenRecordOptionsFrame settingsFrame = new ScreenRecordOptionsFrame(rectangle,
                MenuItemsEnum.SETTINGS.getValue());
        settingsFrame.setListener(this);
    }

    private void confirmReboot() {
        int value = new OptionDialog()
                .setDialogTitle(StringConst.ASK_REBOOT)
                .setMainTitle("")
                .showDialog(frame, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (value == 0) {
            view.onCloseFrame();
            deviceActions.restart();
        }
    }

}
