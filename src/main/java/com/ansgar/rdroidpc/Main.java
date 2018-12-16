package com.ansgar.rdroidpc;

import com.ansgar.rdoidpc.constants.DimensionConst;
import com.ansgar.rdoidpc.constants.ProgrammInf;
import com.ansgar.rdroidpc.ui.MainPanel;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle(ProgrammInf.NAME + "/" + ProgrammInf.VERSION);
        frame.add(new MainPanel());
        frame.setBounds(700, 100,
                DimensionConst.MAIN_WINDOW_WIDTH,
                DimensionConst.MAIN_WINDOW_HEIGHT);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setResizable(false);
    }

}
