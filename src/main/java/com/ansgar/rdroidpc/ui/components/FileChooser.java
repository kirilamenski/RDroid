package com.ansgar.rdroidpc.ui.components;

import com.ansgar.rdroidpc.constants.SharedValuesKey;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.listeners.OnFileChooserListener;
import com.ansgar.rdroidpc.utils.SharedValues;

import javax.swing.*;
import java.awt.*;

public class FileChooser {

    private OnFileChooserListener listener;

    public FileChooser(OnFileChooserListener listener) {
        this.listener = listener;
    }

    public void open(Component component, int mode) {
        JFileChooser chooser = new JFileChooser(SharedValues.get(SharedValuesKey.ADB_PATH, ""));
        chooser.setFileSelectionMode(mode);
        chooser.showDialog(component, StringConst.OK);
        if (chooser.getSelectedFile() != null) {
            if (listener != null) {
                listener.onPathSelected(chooser.getSelectedFile().getAbsolutePath());
            }
        }
    }

}
