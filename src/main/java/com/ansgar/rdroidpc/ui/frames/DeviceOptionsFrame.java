package com.ansgar.rdroidpc.ui.frames;

import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.entities.DeviceOption;

import javax.swing.*;
import java.awt.*;

public class DeviceOptionsFrame extends BasePanel {

    private JComboBox bitRateTf;
    private JComboBox screenResolutionCb;
    private Device device;

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

        String[] bitRates = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "15", "16", "17", "18", "19", "20"};
        bitRateTf = new JComboBox(bitRates);
        bitRateTf.setSelectedIndex(3);
        bitRateTf.setBounds(labelWidth + 5, 0, 120, componentHeight);

        screenResolutionCb = new JComboBox(StringConst.Companion.getSCREEN_RESOLUTION_ARRAY_LIST());
        screenResolutionCb.setSelectedIndex(getSelectedIndex());
        screenResolutionCb.setBounds(labelWidth + 5,
                componentHeight + 5,
                120,
                componentHeight);

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
        add(bitRateTf);
        add(screenResolutionCb);
        add(cancelBtn);
        add(okBtn);
    }

    private DeviceOption createDeviceOption() {
        DeviceOption deviceOption = new DeviceOption();

        double bitRate = Double.valueOf((String) bitRateTf.getSelectedItem());
        deviceOption.setBitRate((int) bitRate);

        String[] resolutionsParams = StringConst.Companion
                .getSCREEN_RESOLUTION_ARRAY_LIST()[screenResolutionCb.getSelectedIndex()]
                .split("x");
        deviceOption.setWidth(Integer.valueOf(resolutionsParams[0]));
        deviceOption.setHeight(Integer.valueOf(resolutionsParams[1]));

        return deviceOption;
    }

    private int getSelectedIndex() {
        String[] screens = StringConst.Companion.getSCREEN_RESOLUTION_ARRAY_LIST();
        for (int i = 0; i < screens.length; i++) {
            String[] screenSizes = screens[i].split("x");
            boolean widthEqual = device.getWidth() == Integer.valueOf(screenSizes[0]);
            boolean heightEqual = device.getHeight() == Integer.valueOf(screenSizes[1]);
            if (widthEqual && heightEqual) return i;
        }

        return 0;
    }

}
