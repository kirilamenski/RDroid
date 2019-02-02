package com.ansgar.rdroidpc.ui.frames;

import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.SharedValuesKey;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.listeners.OnMenuItemListener;
import com.ansgar.rdroidpc.utils.SharedValues;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class SettingsFrame extends BasePanel {

    private JTextField adbPathTf;
    private OnMenuItemListener listener;

    public SettingsFrame(Rectangle rectangle, String title) {
        super(rectangle, title);

        addAdbPath();
        addTextField();
        addFileChooserButton();
    }

    private void addAdbPath() {
        JLabel label = new JLabel("Adb path");
        label.setBounds(25, 0, 125, 50);

        add(label);
    }

    private void addTextField() {
        adbPathTf = new JTextField();
        adbPathTf.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
        adbPathTf.setBounds(150, 10, DimensionConst.SETTINGS_PANEL_WIDTH - 150 - 50, 30);
        adbPathTf.setText(SharedValues.get(SharedValuesKey.ADB_PATH, ""));

        add(adbPathTf);
    }

    private void addFileChooserButton() {
        JToggleButton button = new JToggleButton();
        button.setFocusable(false);
        button.setBounds(DimensionConst.SETTINGS_PANEL_WIDTH - 40, 10, 25, 30);
        button.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(SharedValues.get(SharedValuesKey.ADB_PATH, ""));
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.showDialog(this, StringConst.OK);
            if (chooser.getSelectedFile() != null) {
                String path = chooser.getSelectedFile().getAbsolutePath();
                adbPathTf.setText(path);
                if (listener != null) listener.onAdbPathChanged(path);
            }
        });

        add(button);
    }

    public void setListener(OnMenuItemListener listener) {
        this.listener = listener;
    }
}
