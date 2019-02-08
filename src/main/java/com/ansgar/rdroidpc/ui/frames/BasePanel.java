package com.ansgar.rdroidpc.ui.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class BasePanel extends JPanel implements WindowListener {

    protected JFrame frame;
    private Rectangle rectangle;


    public BasePanel(Rectangle rectangle, String title) {
        setLayout(null);
        this.rectangle = rectangle;
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
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                Component component = e.getComponent();
                rectangle.x = component.getX();
                rectangle.y = component.getY();
            }
        });
    }

    protected void onCloseApp() {

    }

    protected void closeFrame() {
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose();
        }
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

    public Rectangle getRectangle() {
        return rectangle;
    }
}
