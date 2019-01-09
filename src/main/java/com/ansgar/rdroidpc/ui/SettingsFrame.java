package com.ansgar.rdroidpc.ui;

import com.ansgar.rdroidpc.constants.Colors;
import com.ansgar.rdroidpc.constants.DimensionConst;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SettingsFrame extends BasePanel {

    private JTextField adbPathTf;

    public SettingsFrame(Rectangle rectangle, String title) {
        super(rectangle, title);
        setBackground(Color.decode(Colors.MAIN_BACKGROUND_COLOR));

        addAdbPath();
        addTextField();
        addFileChooserButton();
    }

    private void addAdbPath() {
        JLabel label = new JLabel("Adb path");
        label.setForeground(Color.WHITE);
        label.setBounds(25, 0, 125, 50);

        add(label);
    }

    private void addTextField() {
        adbPathTf = new JTextField();
        adbPathTf.setBackground(Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        adbPathTf.setForeground(Color.WHITE);
        adbPathTf.setBounds(150, 10, DimensionConst.SETTINGS_PANEL_WIDTH - 150 - 50, 30);

        add(adbPathTf);
    }

    private void addFileChooserButton() {
        JButton button = new JButton("...");
        button.setFocusable(false);
        button.setBounds(DimensionConst.SETTINGS_PANEL_WIDTH - 50, 10, 30, 30);
        button.addActionListener(e -> {
            UIManager.put("ScrollPane.background", Color.decode(Colors.MAIN_BACKGROUND_COLOR));
            UIManager.put("List.background",  Color.decode(Colors.MAIN_BACKGROUND_COLOR));
            UIManager.put("List.foreground", Color.WHITE);

            JFileChooser chooser = new JFileChooser();
            chooser.addChoosableFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    System.out.println(f.getAbsolutePath());
                    adbPathTf.setText(f.getAbsolutePath());
                    return true;
                }

                @Override
                public String getDescription() {
                    return null;
                }
            });
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.showDialog(this, "Attach");
            adbPathTf.setText(chooser.getCurrentDirectory().getAbsolutePath());
        });

        add(button);
    }

}
