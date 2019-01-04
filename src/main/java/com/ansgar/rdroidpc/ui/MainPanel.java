package com.ansgar.rdroidpc.ui;

import com.android.chimpchat.adb.AdbBackend;
import com.ansgar.rdoidpc.constants.AdbCommandEnum;
import com.ansgar.rdoidpc.constants.Colors;
import com.ansgar.rdoidpc.constants.DimensionConst;
import com.ansgar.rdoidpc.entities.Device;
import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.commands.ResponseParserUtil;
import com.ansgar.rdroidpc.ui.frames.VideoFrame;
import com.ansgar.rdroidpc.ui.components.menu.MenuBar;
import com.ansgar.rdroidpc.utils.StringUtils;
import com.ansgar.rdroidpc.utils.listeners.OnVideoFrameListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainPanel extends JPanel implements OnVideoFrameListener {

    private Color backgroundColor;
    private MenuBar menuBar;
    private JPanel devicesPanel;
    private HashMap<String, VideoFrame> openedDevices;

    public MainPanel() {
        this.backgroundColor = Color.decode(Colors.MAIN_BACKGROUND_COLOR);
        this.menuBar = new MenuBar();
        this.devicesPanel = new JPanel();
        this.openedDevices = new HashMap<>();
        setUpMainPanel();
    }

    private void setUpMainPanel() {
        setLayout(null);
        setBounds(0, 0, DimensionConst.MAIN_WINDOW_WIDTH, DimensionConst.MAIN_WINDOW_HEIGHT);
        setBackground(backgroundColor);
        menuBar = new MenuBar();
        add(menuBar.getMenuBar());

        devicesPanel = new JPanel();
        devicesPanel.setBounds(0, menuBar.getHeight(),
                DimensionConst.MAIN_WINDOW_WIDTH,
                (DimensionConst.MAIN_WINDOW_HEIGHT - menuBar.getHeight()));
        devicesPanel.setBackground(backgroundColor);
        devicesPanel.setLayout(null);


        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.setOnFinishExecuteListener((this::showDevices)
        );
        commandExecutor.execute(AdbCommandEnum.DEVICES);
    }

    private void showDevices(StringBuilder lines) {
        ResponseParserUtil responseUtil = new ResponseParserUtil();
        List<Device> deviceList = responseUtil.getDevices(lines);

        String[] columns = {"Name", "Width", "Height", "Device Id", "Status"};
        String[][] data = new String[deviceList.size()][5];

        for (int i = 0; i < deviceList.size(); i++) {
            Device device = deviceList.get(i);
            responseUtil.setDeviceName(device,
                    String.format(Locale.ENGLISH, AdbCommandEnum.DEVICE_NAME.getCommand(), device.getDeviceId()));
            responseUtil.setDeviceSize(device,
                    String.format(Locale.ENGLISH, AdbCommandEnum.DEVICE_SCREEN_SIZE.getCommand(), device.getDeviceId()));
            data[i] = new String[]{
                    device.getDeviceName(),
                    String.valueOf(device.getWidth()),
                    String.valueOf(device.getHeight()),
                    device.getDeviceId(),
                    device.getDeviceStatus()
            };
        }
        AdbBackend adbBackend = new AdbBackend();

        JTable table = new JTable(data, columns);
        table.setCellEditor(null);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    Device device = deviceList.get(row);
                    if (!openedDevices.containsKey(device.getDeviceId())) {
                        VideoFrame videoFrame = new VideoFrame(device, adbBackend);
                        videoFrame.setOnVideoFrameListener(MainPanel.this);
                        videoFrame.startNewThread(StringUtils.getScreenRecordCommand(device, 4, 45));
                        openedDevices.put(device.getDeviceId(), videoFrame);
                    } else {
                        System.out.println("Device is already opened.");
                    }
                }
            }
        });
        table.getTableHeader().setBackground(backgroundColor);
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 0,
                DimensionConst.MAIN_WINDOW_WIDTH,
                (DimensionConst.MAIN_WINDOW_HEIGHT - menuBar.getHeight()));
        scrollPane.getViewport().setBackground(backgroundColor);
        scrollPane.getViewport().setForeground(Color.WHITE);
        devicesPanel.add(scrollPane);
        add(devicesPanel);
    }

    @Override
    public void onClosed(Device device) {
        if (openedDevices != null) {
            openedDevices.remove(device.getDeviceId());
        }
    }
}
