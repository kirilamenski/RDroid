package com.ansgar.rdroidpc.ui.frames.presenters;

import com.android.chimpchat.core.TouchPressType;
import com.ansgar.rdroidpc.constants.AdbKeyCode;
import com.ansgar.rdroidpc.enums.OrientationEnum;
import com.ansgar.rdroidpc.listeners.DeviceActions;
import com.ansgar.rdroidpc.listeners.OnFileChooserListener;
import com.ansgar.rdroidpc.listeners.impl.DeviceActionsImpl;
import com.ansgar.rdroidpc.ui.components.ButtonsPanel;
import com.ansgar.rdroidpc.ui.components.FileChooser;
import com.ansgar.rdroidpc.ui.frames.VideoFrame;
import com.ansgar.rdroidpc.ui.frames.views.VideoFrameView;
import com.ansgar.rdroidpc.utils.DateUtil;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

public class VideoFramePresenter extends BasePresenter implements OnFileChooserListener {

    private VideoFrameView view;
    private DeviceActions deviceActions;

    public VideoFramePresenter(VideoFrame frame, VideoFrameView view) {
        super(view);
        this.view = view;
        this.deviceActions = new DeviceActionsImpl(frame);
    }

    public ButtonsPanel.OnButtonPanelListener rightActionsListener = id -> {
        switch (id) {
            case 0:
                OrientationEnum orientationEnum = view.getCurrentOrientation() == OrientationEnum.PORTRAIT
                        ? OrientationEnum.LANDSCAPE : OrientationEnum.PORTRAIT;
                view.changeOrientation(orientationEnum);
                break;
            case 1:
                openFileChooser();
                break;
            case 2:
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

    @Override
    public void destroy() {
        if (deviceActions != null) {
            deviceActions.disableAccelerometer(1);
            deviceActions.destroy();
        }
    }

    private void openFileChooser() {
        FileChooser chooser = new FileChooser(this);
        chooser.open(JFileChooser.DIRECTORIES_ONLY);
    }

    @Override
    public void onPathSelected(String path) {
        deviceActions.screenCapture(DateUtil.getCurrentDate() + ".png", path);
    }
}
