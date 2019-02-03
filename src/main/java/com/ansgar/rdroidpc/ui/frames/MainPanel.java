package com.ansgar.rdroidpc.ui.frames;

import com.android.chimpchat.adb.AdbBackend;
import com.ansgar.rdroidpc.constants.*;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.commands.ResponseParserUtil;
import com.ansgar.rdroidpc.listeners.impl.MainPanelMenuListenerImpl;
import com.ansgar.rdroidpc.ui.components.DevicesContainer;
import com.ansgar.rdroidpc.ui.components.menu.MenuBar;
import com.ansgar.rdroidpc.utils.StringUtils;
import com.ansgar.rdroidpc.listeners.OnVideoFrameListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainPanel extends BasePanel implements OnVideoFrameListener, DevicesContainer.OnItemClicked {

    private MenuBar menuBar;
    private HashMap<String, VideoFrame> openedDevices;
    private List<Device> devices;
    private AdbBackend adbBackend;

    public MainPanel(Rectangle rectangle, String title) {
        super(rectangle, title);
        this.menuBar = new MenuBar();
        this.openedDevices = new HashMap<>();
        this.adbBackend = new AdbBackend();
        this.devices = new ArrayList<>();
        setUpMainPanel();
    }

    private void setUpMainPanel() {
        menuBar = new MenuBar();
        menuBar.setListener(new MainPanelMenuListenerImpl(this));
        add(menuBar.getMenuBar(StringConst.Companion.getMenuItems()));

        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.setOnFinishExecuteListener((this::showDevices));
        commandExecutor.execute(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.DEVICES));
    }

    private void showDevices(StringBuilder lines) {
        ResponseParserUtil responseUtil = new ResponseParserUtil();
        openedDevices.clear();
        devices.clear();
        devices.addAll(responseUtil.getDevices(lines));

        for (Device device : devices) {
            responseUtil.setDeviceName(
                    device,
                    String.format(
                            Locale.ENGLISH,
                            AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.DEVICE_NAME),
                            device.getDeviceId()
                    )
            );
            responseUtil.setDeviceSize(device,
                    String.format(
                            Locale.ENGLISH,
                            AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.DEVICE_SCREEN_SIZE),
                            device.getDeviceId()
                    )
            );
        }

        add(createDeviceContainer());
    }

    private DevicesContainer createDeviceContainer() {
        DevicesContainer devicesContainer = new DevicesContainer();
        devicesContainer.setBounds(0, menuBar.getHeight(), getWidth(), getHeight());
        devicesContainer.createContainer(devices, (Object[]) StringConst.Companion.getDeviceHeaderNames());
        devicesContainer.setListener(this);
        return devicesContainer;
    }

    @Override
    public void onCloseApp() {
        super.onCloseApp();
        closeDevicesConnections();
        try {
            stopAdbConnection();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        System.exit(0);
    }

    @Override
    public void onClosed(Device device) {
        if (openedDevices != null) {
            openedDevices.remove(device.getDeviceId());
        }
    }

    @Override
    public void onStartDevice(int position, Device device) {
        if (!openedDevices.containsKey(device.getDeviceId())) {
            VideoFrame videoFrame = new VideoFrame(device, adbBackend);
            videoFrame.setOnVideoFrameListener(MainPanel.this);
            videoFrame.startNewThread(StringUtils.getScreenRecordCommand(device, 12, 45));
            openedDevices.put(device.getDeviceId(), videoFrame);
        } else {
            System.out.println("Device is already opened.");
        }
    }

    @Override
    public void onDeviceSettings(int position, Device device) {
        Rectangle rectangle = new Rectangle(getRectangle().x + 200, getRectangle().y, 400, 400);
        new DeviceOptionsFrame(device, rectangle, StringConst.DEVICE_OPTIONS);
    }

    public void closeDevicesConnections() {
        if (openedDevices.size() > 0) {
            Object[] values = openedDevices.values().toArray();
            for (Object obj : values) {
                if (obj instanceof VideoFrame) {
                    ((VideoFrame) obj).stop(true);
                }
            }
        }

        setUpMainPanel();
    }

    public void stopAdbConnection() throws Exception {
        if (adbBackend != null) {
            adbBackend.shutdown();
        }
    }

}
