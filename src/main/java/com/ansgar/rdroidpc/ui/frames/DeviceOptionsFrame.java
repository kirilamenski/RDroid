package com.ansgar.rdroidpc.ui.frames;

import com.ansgar.filemanager.FileManager;
import com.ansgar.filemanager.FileManagerImpl;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.entities.FilesEnum;
import com.ansgar.rdroidpc.entities.Option;
import com.ansgar.rdroidpc.listeners.OnDeviceOptionListener;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class DeviceOptionsFrame extends BasePanel {

    private FileManager fileManager;
    private Device device;
    private int bitRate = 4;
    private int deviceWidth;
    private int deviceHeight;
    @Nullable
    private OnDeviceOptionListener listener;

    public DeviceOptionsFrame(Component component, Device device, Rectangle rectangle) {
        super(rectangle, String.format("%s Options", device.getDeviceName()));
        this.fileManager = new FileManagerImpl(FilesEnum.CACHE.getValue());
        Device cachedDevice = fileManager.getClass(FilesEnum.DEVICES.getValue(), device.getDeviceId(), Device.class);
        if (cachedDevice == null) {
            this.device = device;
        } else {
            this.device = cachedDevice;
        }
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
        bitRateCb.setSelectedIndex(device.getOption().getBitRate() - 1);

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
        screenResolutionCb.setSelectedIndex(getSelectedIndex(screenSizes));

        JButton okBtn = new JButton(StringConst.OK);
        okBtn.setBounds(getRectangle().width - 225, getRectangle().height - 100, 100, 50);
        okBtn.setFocusable(false);
        okBtn.addActionListener(e -> {
            device.setOption(new Option(bitRate, deviceWidth, deviceHeight));
            fileManager.save(FilesEnum.DEVICES.getValue(), device.getDeviceId(), device);
            if (listener != null) listener.onOptionSelected(device);
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

    private int getSelectedIndex(String[] screens) {
        for (int i = 0; i < screens.length; i++) {
            String[] screenSizes = screens[i].split("x");
            boolean widthEqual = device.getOption().getWidth() == Integer.valueOf(screenSizes[0]);
            boolean heightEqual = device.getOption().getHeight() == Integer.valueOf(screenSizes[1]);
            if (widthEqual && heightEqual) return i;
        }

        return 0;
    }

    public void setListener(@Nullable OnDeviceOptionListener listener) {
        this.listener = listener;
    }

}
