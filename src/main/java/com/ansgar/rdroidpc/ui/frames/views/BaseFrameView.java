package com.ansgar.rdroidpc.ui.frames.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public interface BaseFrameView {
    Rectangle getRectangle();

    JComponent getChildComponent();

    void setKeyboardListener(KeyListener listener);

    void onCloseFrame();
}
