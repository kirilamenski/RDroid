package com.ansgar.rdroidpc.ui;

import com.android.chimpchat.adb.AdbBackend;
import com.ansgar.rdroidpc.constants.AdbCommandEnum;
import com.ansgar.rdroidpc.constants.Colors;
import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.commands.ResponseParserUtil;
import com.ansgar.rdroidpc.listeners.OnMenuItemListener;
import com.ansgar.rdroidpc.ui.components.DevicesContainer;
import com.ansgar.rdroidpc.ui.frames.VideoFrame;
import com.ansgar.rdroidpc.ui.components.menu.MenuBar;
import com.ansgar.rdroidpc.utils.StringUtils;
import com.ansgar.rdroidpc.listeners.OnVideoFrameListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainPanel extends JPanel implements OnVideoFrameListener, DevicesContainer.OnItemClicked, OnMenuItemListener {

    private Color backgroundColor;
    private MenuBar menuBar;
    private HashMap<String, VideoFrame> openedDevices;
    private List<Device> devices;
    private AdbBackend adbBackend;


    public MainPanel() {
        this.backgroundColor = Color.decode(Colors.MAIN_BACKGROUND_COLOR);
        this.menuBar = new MenuBar();
        this.openedDevices = new HashMap<>();
        this.adbBackend = new AdbBackend();
        this.devices = new ArrayList<>();
        setUpMainPanel();
    }

    private void setUpMainPanel() {
        setLayout(null);
        setBounds(0, 0, DimensionConst.MAIN_WINDOW_WIDTH, DimensionConst.MAIN_WINDOW_HEIGHT);
        setBackground(backgroundColor);
        menuBar = new MenuBar();
        menuBar.setListener(this);
        add(menuBar.getMenuBar(StringConst.Companion.getMenuItems()));

        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.setOnFinishExecuteListener((this::showDevices));
        commandExecutor.execute(AdbCommandEnum.DEVICES);
    }

    private void showDevices(StringBuilder lines) {
        ResponseParserUtil responseUtil = new ResponseParserUtil();
        devices = responseUtil.getDevices(lines);

        for (Device device : devices) {
            responseUtil.setDeviceName(device,
                    String.format(Locale.ENGLISH, AdbCommandEnum.DEVICE_NAME.getCommand(), device.getDeviceId()));
            responseUtil.setDeviceSize(device,
                    String.format(Locale.ENGLISH, AdbCommandEnum.DEVICE_SCREEN_SIZE.getCommand(), device.getDeviceId()));
        }

        add(createDeviceContainer());

    }

    private DevicesContainer createDeviceContainer() {
        DevicesContainer devicesContainer = new DevicesContainer();
        devicesContainer.setBackground(backgroundColor);
        devicesContainer.setBounds(0, menuBar.getHeight(), getWidth(), getHeight());
        devicesContainer.createContainer(devices, (Object[]) StringConst.Companion.getDEVICES_CONTAINER_HEADER_NAMES());
        devicesContainer.setListener(this);
        return devicesContainer;
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

    }

    // TODO Can be moved to another listener class
    @Override
    public void onItemClicked(String name) {
        System.out.println(name);
        switch (name) {
            case "Exit":
                // TODO in exit block stop all opened connections and restart adb server
                adbBackend.shutdown();
                System.exit(0);
                break;
        }
    }
}
