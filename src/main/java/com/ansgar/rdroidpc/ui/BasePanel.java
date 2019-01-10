package com.ansgar.rdroidpc.ui;

import javax.swing.*;
import java.awt.*;

public class BasePanel extends JPanel {

    public BasePanel(Rectangle rectangle, String title) {
        setLayout(null);
        initFrame(rectangle, title);
    }

    private void initFrame(Rectangle rectangle, String title) {
        JFrame frame = new JFrame(title);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        frame.setResizable(false);
        frame.setBounds(rectangle);
    }

}
