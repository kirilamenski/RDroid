package com.ansgar.rdroidpc.ui.frames;

import com.android.chimpchat.adb.AdbBackend;
import com.ansgar.rdroidpc.constants.*;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.commands.ResponseParserUtil;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.enums.MainMenuItemsEnum;
import com.ansgar.rdroidpc.listeners.DeviceActions;
import com.ansgar.rdroidpc.listeners.MainActionPanelsListener;
import com.ansgar.rdroidpc.listeners.OnMenuItemListener;
import com.ansgar.rdroidpc.listeners.impl.DeviceActionsImpl;
import com.ansgar.rdroidpc.ui.components.ButtonsPanel;
import com.ansgar.rdroidpc.ui.components.DevicesContainer;
import com.ansgar.rdroidpc.ui.components.SpinnerDialog;
import com.ansgar.rdroidpc.ui.components.menu.MenuBar;
import com.ansgar.rdroidpc.listeners.OnVideoFrameListener;
import com.ansgar.rdroidpc.ui.frames.presenters.MainPanelPresenter;
import com.ansgar.rdroidpc.ui.frames.views.MainPanelView;

import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

import static com.ansgar.rdroidpc.constants.DimensionConst.DEFAULT_WIDTH;

public class MainPanel extends BasePanel<MainPanelPresenter> implements MainPanelView,
        OnVideoFrameListener, DevicesContainer.OnItemClicked, OnMenuItemListener {

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
        this.devices = new ArrayList<>();
        this.listener = new MainActionPanelsListener(this);
        new SpinnerDialog(rectangle) {
            @Override
            public void doInBack() {
                adbBackend = new AdbBackend();
                setUpMainPanel();
            }
        }.execute();
    }

    private void setUpMainPanel() {
        menuBar = new MenuBar();
        menuBar.setListener(this);
        add(menuBar.getMenuBar(
                StringConst.Companion.getMainMenuItems(),
                new Rectangle(0, 0, DimensionConst.MAIN_WINDOW_WIDTH, 25)
        ));
        add(getActionsPanel());
        executeAdbDevices();
        updateUI();
    }

    public void executeAdbDevices() {
        DeviceActions actions = new DeviceActionsImpl();
        showDevices(actions.getConnectedDevices());
    }

    public void killServer() {
        if (devicesContainer != null) remove(devicesContainer);
        repaint();
        DeviceActions actions = new DeviceActionsImpl();
        actions.killServer();
    }

    private void showDevices(List<Device> connectedDevices) {
        ResponseParserUtil responseUtil = new ResponseParserUtil();
        openedDevices.clear();
        devices.clear();
        devices.addAll(connectedDevices);

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
        panel.setIcons("icons/ic_restart_64.png", "icons/ic_kill_server_64.png");
        panel.setToolTips("Restart", "Kill Server");
        panel.setBounds(0, menuBar.getHeight(), 96, 48);
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
    public void openSettings() {
        presenter.openSettings();
    }

    @Override
    public void openInformation() {
        presenter.openInformation();
    }

    @Override
    public void onCloseFrame() {
        new SpinnerDialog(getRectangle()) {
            @Override
            public void doInBack() {
                closeDevicesConnections();
                try {
                    stopAdbConnection();
                } catch (NullPointerException ignored) {
                }
                restartServer();
                System.exit(0);
            }
        }.execute();
    }

    @Override
    public void onDeviceConnectionClosed(Device device) {
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
            videoFrame.startNewThread();
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

    @Override
    public MainPanelPresenter createPresenter() {
        return new MainPanelPresenter(this);
    }

    @Override
    public void closeDevicesConnections() {
        if (openedDevices.size() > 0) {
            openedDevices.values().forEach(obj -> {
                if (obj != null) {
                    obj.stop(false);
                }
            });
        }
    }

    private void stopAdbConnection() throws NullPointerException {
        if (adbBackend != null) {
            adbBackend.shutdown();
        }
    }

    private void restartServer() {
        DeviceActions actions = new DeviceActionsImpl();
        actions.killServer();
        actions.startServer();
    }

    @Override
    public void onMenuItemClicked(String name) {
        MainMenuItemsEnum.Companion.execute(name, this);
    }
}
