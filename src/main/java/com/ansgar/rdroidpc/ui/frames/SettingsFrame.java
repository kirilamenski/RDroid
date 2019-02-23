package com.ansgar.rdroidpc.ui.frames;

import com.ansgar.rdroidpc.constants.Colors;
import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.SharedValuesKey;
import com.ansgar.rdroidpc.listeners.OnFileChooserListener;
import com.ansgar.rdroidpc.listeners.OnMenuItemListener;
import com.ansgar.rdroidpc.ui.components.FileChooser;
import com.ansgar.rdroidpc.utils.SharedValues;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class SettingsFrame extends BasePanel implements OnFileChooserListener {

    private JTextField adbPathTf;
    private OnMenuItemListener listener;

    public SettingsFrame(Component component, Rectangle rectangle, String title) {
        super(rectangle, title);
        addAdbFilePathOption();
        relativeTo(component);
    }

    private void addAdbFilePathOption() {
        JLabel label = new JLabel("Adb path");
        label.setBounds(10, 10, 200, 25);
        add(label);

        adbPathTf = new JTextField();
        adbPathTf.setBorder(new MatteBorder(1, 1, 1, 1, Color.decode(Colors.BORDER_COLOR)));
        adbPathTf.setBounds(200, 10,
                DimensionConst.SETTINGS_PANEL_WIDTH - 235, 25);
        adbPathTf.setText(SharedValues.get(SharedValuesKey.ADB_PATH, ""));
        add(adbPathTf);

        JToggleButton toggleBtn = new JToggleButton("...");
        toggleBtn.setFocusable(false);
        toggleBtn.setBounds(DimensionConst.SETTINGS_PANEL_WIDTH - 35, 10, 20, 25);
        toggleBtn.addActionListener(e -> {
            FileChooser fileChooser = new FileChooser(this);
            fileChooser.open(this, JFileChooser.FILES_AND_DIRECTORIES);
        });
        add(toggleBtn);
    }

    public void setListener(OnMenuItemListener listener) {
        this.listener = listener;
    }

    @Override
    public void onPathSelected(String path) {
        adbPathTf.setText(path);
        if (listener != null && path.contains("adb")) {
            listener.onAdbPathChanged(path);
        } else {

        }
    }
}
