package com.ansgar.rdroidpc.ui.components;

import com.ansgar.rdroidpc.constants.SharedValuesKey;
import com.ansgar.rdroidpc.listeners.OnInputPackageListener;
import com.ansgar.rdroidpc.utils.SharedValues;

import javax.swing.*;
import java.util.Vector;

public class InputFieldComponent extends JPanel {

    private OnInputPackageListener listener;
    private JComboBox packagesCb;
    private JButton btn;
    private Vector values;
    private DefaultComboBoxModel<String> defaultComboBoxModel;

    public InputFieldComponent() {
        this.values = SharedValues.get(SharedValuesKey.PACKAGES, new Vector());
        this.defaultComboBoxModel = new DefaultComboBoxModel<String>(values);
        defaultComboBoxModel.addElement("com.fishbowlmedia.fishbowl.dev");
        setLayout(null);
        updateComboBox();
    }

    public void updateComponent() {
        updateComboBox();
        updateRunBtn();
        updateUI();
    }

    private void updateComboBox() {
        if (packagesCb == null) {
            packagesCb = new JComboBox<>(defaultComboBoxModel);
            packagesCb.setEditable(true);
            add(packagesCb);
        }
        packagesCb.setBounds(0, 0, (int) (getWidth() * 0.7), getHeight() - 10);
    }

    private void updateRunBtn() {
        if (btn == null) {
            btn = new JButton();
            btn.setText("Run");
            btn.setFocusable(false);
            btn.addActionListener(e -> {
                if (listener != null) {
                    String value = String.valueOf(packagesCb.getEditor().getItem());

                    if (value == null) {
                        value = (String) packagesCb.getSelectedItem();
                    }
                    if (value != null && value.length() > 0) {
                        addItemToList(value);
                        listener.runDumpsys(value);
                    }
                }
            });
            add(btn);
        }
        btn.setBounds(packagesCb.getWidth(), 0, (int) (getWidth() * 0.30), getHeight() - 10);
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
