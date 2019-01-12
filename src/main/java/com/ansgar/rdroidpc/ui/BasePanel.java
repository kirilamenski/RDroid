package com.ansgar.rdroidpc.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class BasePanel extends JPanel implements WindowListener {

    protected JFrame frame;

    public BasePanel(Rectangle rectangle, String title) {
        setLayout(null);
        initFrame(rectangle, title);
    }

    private void initFrame(Rectangle rectangle, String title) {
        frame = new JFrame(title);
        frame.pack();
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        frame.setResizable(false);
        frame.setBounds(rectangle);
        frame.add(this);
        frame.addWindowListener(this);
    }

    protected void onCloseApp() {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        onCloseApp();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
