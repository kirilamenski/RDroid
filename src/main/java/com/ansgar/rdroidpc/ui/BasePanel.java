package com.ansgar.rdroidpc.ui;

import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.MenuItemsEnum;

import javax.swing.*;
import java.awt.*;

public class BasePanel extends JPanel {

    public BasePanel(Rectangle rectangle, String title) {
        setLayout(null);
        initFrame(rectangle, title);
    }

    private void initFrame(Rectangle rectangle, String title) {
        JFrame frame = new JFrame(MenuItemsEnum.SETTINGS.getValue());
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        frame.setResizable(false);
        frame.setBounds(rectangle);
    }

}
