package com.ansgar.rdroidpc.ui.components;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class DumpsysReportComponent extends JPanel {

    private JTextArea dumpsysTextArea;
    private JScrollPane scrollPane;

    public DumpsysReportComponent(String dumpsysInformation) {
        setLayout(new BorderLayout());
        updatePanel(dumpsysInformation);
    }

    public void updatePanel(String dumpsysInformation) {
        if (scrollPane == null) {
            dumpsysTextArea = new JTextArea(dumpsysInformation);
            dumpsysTextArea.setEditable(false);
            scrollPane = new JScrollPane(dumpsysTextArea);
            scrollPane.setBorder(new MatteBorder(0, 0, 0, 0, Color.BLACK));
            add(scrollPane);
        } else {
            dumpsysTextArea.setText(dumpsysInformation);
        }
        updateUI();
    }

}
