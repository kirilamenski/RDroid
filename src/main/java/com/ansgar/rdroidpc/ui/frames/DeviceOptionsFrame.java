package com.ansgar.rdroidpc.ui.frames;

import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.entities.Option;

import javax.swing.*;
import java.awt.*;

public class DeviceOptionsFrame extends BasePanel {

    private Device device;
    private int bitRate = 4;
    private int deviceWidth = DimensionConst.DEFAULT_WIDTH;
    private int deviceHeight = DimensionConst.DEVICE_CONTAINER_HEIGHT;

    public DeviceOptionsFrame(Device device, Rectangle rectangle, String title) {
        super(rectangle, title);
        this.device = device;
        createFrame();
    }

    private void createFrame() {
        int labelWidth = getRectangle().width - 200;
        int componentHeight = 20;
        JLabel bitRateLabel = new JLabel(StringConst.BIT_RATE_L);
        bitRateLabel.setBounds(0, 0, labelWidth, componentHeight);
        JLabel screenResolutionLabel = new JLabel(StringConst.SCREEN_RESOLUTION_L);
        screenResolutionLabel.setBounds(0, componentHeight + 5, labelWidth, componentHeight);

        JComboBox bitRateCb = new JComboBox<>(StringConst.Companion.getBitRates());
        bitRateCb.setSelectedIndex(3);
        bitRateCb.setBounds(labelWidth + 5, 0, 120, componentHeight);
        bitRateCb.addActionListener(e -> {
            int index = bitRateCb.getSelectedIndex();
            bitRate = Integer.valueOf(StringConst.Companion.getBitRates()[index]);
        });

        JComboBox screenResolutionCb = new JComboBox<>(StringConst.Companion.getDefaultScreenSizes());
        screenResolutionCb.setSelectedIndex(getSelectedIndex());
        screenResolutionCb.setBounds(labelWidth + 5,
                componentHeight + 5,
                120,
                componentHeight);
        screenResolutionCb.addActionListener(e -> {
            int index = screenResolutionCb.getSelectedIndex();
            String[] sizes = StringConst.Companion.getDefaultScreenSizes()[index].split("x");
            deviceWidth = Integer.valueOf(sizes[0]);
            deviceHeight = Integer.valueOf(sizes[1]);
        });

        JButton okBtn = new JButton(StringConst.OK);
        System.out.println(getRectangle().width + " x " + getRectangle().getWidth());
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
        Option option = new Option();
        option.setBitRate(bitRate);
        option.setWidth(deviceWidth);
        option.setHeight(deviceHeight);
        return option;
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
