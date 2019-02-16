package com.ansgar.rdroidpc.ui.frames;

import com.ansgar.rdroidpc.ui.frames.presenters.BasePresenter;
import com.ansgar.rdroidpc.ui.frames.views.BaseFrameView;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public abstract class BasePanel extends JPanel implements WindowListener, BaseFrameView {

    private Rectangle rectangle;
    protected JFrame frame;

    public BasePanel(Rectangle rectangle, String title) {
        setLayout(null);
        this.rectangle = rectangle;
        initFrame(rectangle, title);
        createPresenter();
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

    protected void closeFrame() {
        if (getPresenter() != null) {
            getPresenter().destroy();
        }
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose();
        }
    }

    @Override
    public void onCloseFrame() {

    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        onCloseFrame();
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

    public void createPresenter() {
    }

    @Nullable
    public BasePresenter getPresenter() {
        return null;
    }

}
