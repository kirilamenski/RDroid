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

    public void open(Component component, int mode, String directoryPath) {
        JFileChooser chooser = new JFileChooser(directoryPath);
        chooser.setFileSelectionMode(mode);
        chooser.showDialog(component, StringConst.OK);
        if (chooser.getSelectedFile() != null) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            SharedValues.put(SharedValuesKey.DEFAULT_FILE_UPLOADING_FOLDER, path);
            if (listener != null) listener.onPathSelected(path);
        }
    }

}
