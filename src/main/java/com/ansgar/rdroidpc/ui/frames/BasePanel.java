package com.ansgar.rdroidpc.ui.frames;

import com.ansgar.rdroidpc.listeners.OnWindowResizedListener;
import com.ansgar.rdroidpc.ui.components.OptionDialog;
import com.ansgar.rdroidpc.ui.frames.presenters.BasePresenter;
import com.ansgar.rdroidpc.ui.frames.views.BaseFrameView;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class BasePanel<T extends BasePresenter> extends JPanel implements WindowListener, BaseFrameView {

    protected T presenter;
    protected JFrame frame;
    private Rectangle rectangle;
    private boolean resizable;
    private OnWindowResizedListener listener;

    public BasePanel(Rectangle rectangle, String title) {
        this.rectangle = rectangle;
        setBounds(rectangle);
        setLayout(null);
        initFrame(rectangle, title, false);
        presenter = createPresenter();
    }

    public BasePanel(Rectangle rectangle, String title, boolean resizable) {
        this.rectangle = rectangle;
        this.resizable = resizable;
        setBounds(rectangle);
        setLayout(null);
        initFrame(rectangle, title, false);
        presenter = createPresenter();
    }

    public BasePanel(JComponent component, String title, boolean undecorated) {
        this.rectangle = component.getBounds();
        setBounds(rectangle);
        setLayout(null);
        initFrame(rectangle, title, undecorated);
        presenter = createPresenter();
    }

    private void initFrame(Rectangle rectangle, String title, boolean undecorated) {
        frame = new JFrame(title);
        frame.setUndecorated(undecorated);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        frame.setResizable(resizable);
        frame.setPreferredSize(new Dimension(rectangle.width, rectangle.height));
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.addWindowListener(this);
        frame.pack();
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                Component component = e.getComponent();
                rectangle.x = component.getBounds().x;
                rectangle.y = component.getBounds().y;
            }

            @Override
            public void componentResized(ComponentEvent e) {
                if (listener != null) {
                    listener.windowResized(new Rectangle(e.getComponent().getBounds()));
                }
            }
        });
    }

    protected void closeFrame() {
        if (presenter != null) {
            presenter.destroy();
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
    public int showMessageDialog(String title, String message, int optionType, int messageType) {
        return new OptionDialog()
                .setDialogTitle(message)
                .setMainTitle(title)
                .showDialog(this, optionType, messageType);
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

    public T createPresenter() {
        return null;
    }

    protected void relativeTo(Component component) {
        if (frame != null) frame.setLocationRelativeTo(component);
    }

    public void setWindowResizedListener(OnWindowResizedListener listener) {
        this.listener = listener;
    }

}
