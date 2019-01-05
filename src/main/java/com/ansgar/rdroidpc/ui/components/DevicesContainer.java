package com.ansgar.rdroidpc.ui.components;

import com.ansgar.rdoidpc.constants.Colors;
import com.ansgar.rdoidpc.constants.DimensionConst;
import com.ansgar.rdoidpc.entities.Device;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.List;

public class DevicesContainer extends JPanel {

    private OnItemClicked listener;
    private List<Device> devices;

    public DevicesContainer createContainer(List<Device> devices, Object... headerNames) {
        this.devices = devices;
        setLayout(null);
        add(createChildContainer(-1, 0, headerNames));
        for (int i = 0; i < devices.size(); i++) {
            Device device = devices.get(i);
            Object[] values = {device.getDeviceName(),
                    String.format("%dx%d", device.getWidth(), device.getHeight()),
                    device.getDeviceId(),
                    device.getDeviceStatus(),
                    true
            };
            add(createChildContainer(i, DimensionConst.DEVICE_CONTAINER_HEIGHT * (i + 1), values));
        }
        return this;
    }

    private JPanel createChildContainer(int position, int yPos, Object... values) {
        JPanel headerContainer = new JPanel();
        headerContainer.setBackground(getBackground());
        headerContainer.setBounds(getX(), yPos, getWidth(), DimensionConst.DEVICE_CONTAINER_HEIGHT);
        headerContainer.setLayout(null);

        int itemWidth = headerContainer.getWidth() / values.length;

        for (int i = 0; i < values.length; i++) {
            Object obj = values[i];
            if (obj instanceof String) {
                JLabel label = new JLabel((String) obj);
                label.setBounds(itemWidth * i, yPos, itemWidth, headerContainer.getHeight());
                label.setForeground(Color.WHITE);
                label.setBorder(new MatteBorder(0, 1, 1, 0, Color.BLACK));
                label.setFont(new Font(label.getName(), Font.PLAIN, 12));
                label.setVerticalAlignment(JLabel.CENTER);
                label.setHorizontalAlignment(JLabel.CENTER);
                add(label);
            } else if (obj instanceof Boolean) {
                add(createActionButtons(position, itemWidth * i, yPos, itemWidth, headerContainer.getHeight()));
            }
        }

        return headerContainer;
    }

    private JPanel createActionButtons(int position, int x, int y, int width, int height) {
        ButtonsPanel panel = new ButtonsPanel();
        panel.setIcons("icons/ic_settings_64.png", "icons/ic_run_64.png");
        panel.setBackground(Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        panel.setBounds(x, y, width, height);
        panel.setItemClickListener((id) -> {
            if (listener != null) {
                if (id == 0) {

                } else if (id == 1) {
                    listener.onStartDevice(position, devices.get(position));
                }
            }
        });
        panel.createPanel();

        return panel;
    }

    public void setListener(OnItemClicked listener) {
        this.listener = listener;
    }

    public interface OnItemClicked {
        void onStartDevice(int position, Device device);

        void onDeviceSettings(int position, Device device);
    }
}
