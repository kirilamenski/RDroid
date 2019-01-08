package com.ansgar.rdroidpc.ui;

import com.ansgar.rdroidpc.constants.Colors;

import javax.swing.*;
import java.awt.*;

public class SettingsFrame extends BasePanel {

    public SettingsFrame(Rectangle rectangle, String title) {
        super(rectangle, title);
        setBackground(Color.decode(Colors.MAIN_BACKGROUND_COLOR));

        addAdbPath();
    }

    private void addAdbPath() {
        JLabel label = new JLabel("Adb path");
        label.setForeground(Color.WHITE);
        label.setBounds(0, 0, 150, 50);

        add(label);
    }

}
