package com.ansgar.rdroidpc.ui.frames;

import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.entities.Option;

import javax.swing.*;
import java.awt.*;

public class DeviceOptionsFrame extends BasePanel {

    private Device device;
    private int bitRate = 4;
    private int deviceWidth;
    private int deviceHeight;

    public DeviceOptionsFrame(Component component, Device device, Rectangle rectangle) {
        super(rectangle, String.format("%s Options", device.getDeviceName()));
        this.device = device;
        createFrame();
        relativeTo(component);
    }

    private void createFrame() {
        int labelWidth = getRectangle().width - 200;
        int componentHeight = 20;
        JLabel bitRateLabel = new JLabel(StringConst.BIT_RATE_L);
        bitRateLabel.setBounds(10, 10, labelWidth, componentHeight);
        JLabel screenResolutionLabel = new JLabel(StringConst.SCREEN_RESOLUTION_L);
        screenResolutionLabel.setBounds(10, componentHeight + 15, labelWidth, componentHeight);

        String[] bitRates = StringConst.Companion.getBitRates();
        JComboBox bitRateCb = new JComboBox<>(bitRates);
        bitRateCb.setBounds(
                getRectangle().width - 135,
                10,
                120,
                componentHeight
        );
        bitRateCb.addActionListener(e -> {
            int index = bitRateCb.getSelectedIndex();
            bitRate = Integer.valueOf(StringConst.Companion.getBitRates()[index]);
        });
        bitRateCb.setSelectedIndex(3); // TODO change when will be added possibility to save device option in file

        String[] screenSizes = StringConst.Companion.getDefaultScreenSizes();
        JComboBox screenResolutionCb = new JComboBox<>(screenSizes);
        screenResolutionCb.setBounds(
                getRectangle().width - 135,
                componentHeight + 15,
                120,
                componentHeight
        );
        screenResolutionCb.addActionListener(e -> {
            int index = screenResolutionCb.getSelectedIndex();
            String[] sizes = screenSizes[index].split("x");
            deviceWidth = Integer.valueOf(sizes[0]);
            deviceHeight = Integer.valueOf(sizes[1]);
        });
        screenResolutionCb.setSelectedIndex(getSelectedIndex());

        JButton okBtn = new JButton(StringConst.OK);
        okBtn.setBounds(getRectangle().width - 225, getRectangle().height - 100, 100, 50);
        okBtn.setFocusable(false);
        okBtn.addActionListener(e -> {
            device.setOption(createDeviceOption());
            closeFrame();
        });

        JButton cancelBtn = new JButton(StringConst.CANCEL);
        cancelBtn.setBounds(getRectangle().width - 115, getRectangle().height - 100, 100, 50);
        cancelBtn.setFocusable(false);
        cancelBtn.addActionListener(e -> closeFrame());

        add(bitRateLabel);
        add(screenResolutionLabel);
        add(bitRateCb);
        add(screenResolutionCb);
        add(cancelBtn);
        add(okBtn);
    }

    private Option createDeviceOption() {
        return new Option(bitRate, deviceWidth, deviceHeight);
    }

    private int getSelectedIndex() {
        String[] screens = StringConst.Companion.getDefaultScreenSizes();
        for (int i = 0; i < screens.length; i++) {
            String[] screenSizes = screens[i].split("x");
            boolean widthEqual = device.getWidth() == Integer.valueOf(screenSizes[0]);
            boolean heightEqual = device.getHeight() == Integer.valueOf(screenSizes[1]);
            if (widthEqual && heightEqual) return i;
        }

        return 0;
    }

}
