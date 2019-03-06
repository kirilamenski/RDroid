package com.ansgar.rdroidpc.ui.frames;

import com.ansgar.rdroidpc.ui.frames.presenters.BasePresenter;
import com.ansgar.rdroidpc.ui.frames.views.BaseFrameView;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class BasePanel extends JPanel implements WindowListener, BaseFrameView {

    private Rectangle rectangle;
    protected JFrame frame;

    public BasePanel(Rectangle rectangle, String title) {
        this.rectangle = rectangle;
        setBounds(rectangle);
        setLayout(null);
        initFrame(rectangle, title, false);
        createPresenter();
    }

    public BasePanel(JComponent component, String title, boolean undecorated) {
        this.rectangle = component.getBounds();
        setBounds(rectangle);
        setLayout(null);
        initFrame(rectangle, title, undecorated);
        createPresenter();
    }

    private void initFrame(Rectangle rectangle, String title, boolean undecorated) {
        frame = new JFrame(title);
        frame.setUndecorated(undecorated);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(rectangle.width, rectangle.height));
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.addWindowListener(this);
        frame.pack();
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

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public void setKeyboardListener(KeyListener listener) {
        frame.addKeyListener(listener);
    }

    @Override
    public JComponent getChildComponent() {
        return this;
    }

    @Override
    public JFrame getParentComponent() {
        return frame;
    }

    public void createPresenter() {
    }

    @Nullable
    public BasePresenter getPresenter() {
        return null;
    }

    protected void relativeTo(Component component) {
        if (frame != null) frame.setLocationRelativeTo(component);
    }

}
