package com.ansgar.rdroidpc.ui.components;

import com.ansgar.rdroidpc.constants.SharedValuesKey;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.listeners.OnInputPackageListener;
import com.ansgar.rdroidpc.utils.SharedValues;

import javax.swing.*;
import java.util.List;
import java.util.Vector;

public class InputFieldComponent extends JPanel {

    private OnInputPackageListener listener;
    private JComboBox packagesCb;
    private JButton btn;
    private Vector values;
    private DefaultComboBoxModel<String> defaultComboBoxModel;
    private boolean isPressed = false;

    public InputFieldComponent(List<String> packages) {
        this.values = SharedValues.get(SharedValuesKey.PACKAGES, new Vector());
        this.defaultComboBoxModel = new DefaultComboBoxModel<String>(values);
        for (String pack : packages) {
            defaultComboBoxModel.addElement(pack);
        }
        setLayout(null);
    }

    public void updateComponent() {
        updateRunBtn();
        updateComboBox();
        updateUI();
    }

    private void updateComboBox() {
        if (packagesCb == null) {
            packagesCb = new JComboBox<>(defaultComboBoxModel);
            packagesCb.setEditable(true);
            add(packagesCb);
        }
        packagesCb.setBounds(0, 0, btn.getX() - 10, getHeight() - 10);
    }

    private void updateRunBtn() {
        if (btn == null) {
            btn = new JButton();
            btn.setText(StringConst.RUN);
            btn.setFocusable(false);
            btn.addActionListener(e -> {
                if (listener != null) {
                    if (!isPressed) {
                        String value = String.valueOf(packagesCb.getEditor().getItem());
                        if (value == null) value = (String) packagesCb.getSelectedItem();
                        if (value != null && value.length() > 0) {
                            addItemToList(value);
                            listener.runDumpsys(value);
                            btn.setText(StringConst.PAUSE);
                        }
                        isPressed = true;
                    } else {
                        btn.setText(StringConst.RUN);
                        listener.stopDumpsys();
                        isPressed = false;
                    }
                }
            });
            add(btn);
        }
        btn.setBounds(getWidth() - 100, 0, 100, getHeight() - 10);
    }

    private void addItemToList(String value) {
        if (!values.contains(value)) {
            defaultComboBoxModel.addElement(value);
            SharedValues.put(SharedValuesKey.PACKAGES, values);
        }
    }

    public void setListener(OnInputPackageListener listener) {
        this.listener = listener;
    }
}
