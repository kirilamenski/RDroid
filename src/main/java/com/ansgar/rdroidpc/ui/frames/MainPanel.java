package com.ansgar.rdroidpc.ui.frames;

import com.android.chimpchat.adb.AdbBackend;
import com.ansgar.rdroidpc.constants.*;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.commands.ResponseParserUtil;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.listeners.MainActionPanelsListener;
import com.ansgar.rdroidpc.listeners.impl.MainPanelMenuListenerImpl;
import com.ansgar.rdroidpc.ui.components.ButtonsPanel;
import com.ansgar.rdroidpc.ui.components.DevicesContainer;
import com.ansgar.rdroidpc.ui.components.menu.MenuBar;
import com.ansgar.rdroidpc.utils.StringUtils;
import com.ansgar.rdroidpc.listeners.OnVideoFrameListener;

import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

import static com.ansgar.rdroidpc.constants.DimensionConst.DEFAULT_WIDTH;

public class MainPanel extends BasePanel implements OnVideoFrameListener, DevicesContainer.OnItemClicked {

    private MenuBar menuBar;
    private DevicesContainer devicesContainer;
    private MainActionPanelsListener listener;
    private HashMap<String, VideoFrame> openedDevices;
    private List<Device> devices;
    private AdbBackend adbBackend;

    public MainPanel(Rectangle rectangle, String title) {
        super(rectangle, title);
        this.menuBar = new MenuBar();
        this.openedDevices = new HashMap<>();
        this.adbBackend = new AdbBackend();
        this.devices = new ArrayList<>();
        this.listener = new MainActionPanelsListener(this);
        setUpMainPanel();
    }

    private void setUpMainPanel() {
        menuBar = new MenuBar();
        menuBar.setListener(new MainPanelMenuListenerImpl(this));
        add(menuBar.getMenuBar(StringConst.Companion.getMenuItems()));
        add(getActionsPanel());
        executeAdbDevices();
    }

    public void executeAdbDevices() {
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
            responseUtil.setDeviceOption(device);
        }
        createDeviceContainer();
    }

    private ButtonsPanel getActionsPanel() {
        ButtonsPanel panel = new ButtonsPanel();
        panel.setIcons("icons/ic_restart_64.png");
        panel.setBounds(0, menuBar.getHeight(), 48, 48);
        panel.setIconSize(30, 30);
        panel.setBorder(new MatteBorder(0, 0, 0, 0, Color.BLACK));
        panel.setItemClickListener(listener);
        panel.createPanel();
        return panel;
    }

    private void createDeviceContainer() {
        if (devicesContainer != null) remove(devicesContainer);
        devicesContainer = new DevicesContainer();
        devicesContainer.setBounds(0, menuBar.getHeight() + 48, getWidth(), getHeight());
        devicesContainer.createContainer(devices, (Object[]) StringConst.Companion.getDeviceHeaderNames());
        devicesContainer.setListener(this);
        add(devicesContainer);
        repaint();
    }

    @Override
    public void onCloseFrame() {
        closeDevicesConnections();
        try {
            stopAdbConnection();
        } catch (NullPointerException ignored) {
        }
        restartServer();
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
        Rectangle rectangle = new Rectangle(
                getRectangle().x,
                getRectangle().y,
                DEFAULT_WIDTH / 2,
                (int) (DEFAULT_WIDTH / DimensionConst.SCREEN_RATIO / 2)
        );
        if (!openedDevices.containsKey(device.getDeviceId())) {
            VideoFrame videoFrame = new VideoFrame(device, adbBackend, rectangle);
            videoFrame.setOnVideoFrameListener(MainPanel.this);
            videoFrame.startNewThread(StringUtils.getScreenRecordCommand(device, 12, 45));
            openedDevices.put(device.getDeviceId(), videoFrame);
        } else {
            System.out.println("Device is already opened.");
        }
    }

    @Override
    public void onDeviceSettings(int position, Device device) {
        Rectangle rectangle = new Rectangle(getRectangle().x, getRectangle().y, 400, 400);
        new DeviceOptionsFrame(this, device, rectangle);
    }

    public void closeDevicesConnections() {
        if (openedDevices.size() > 0) {
            openedDevices.values().forEach(obj -> {
                if (obj != null) {
                    obj.stop(false);
                }
            });
        }
    }

    public void stopAdbConnection() throws NullPointerException {
        if (adbBackend != null) {
            adbBackend.shutdown();
        }
    }

    public void restartServer() {
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.execute(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.KILL_SERVER));
        commandExecutor.execute(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.START_SERVER));
    }

}
