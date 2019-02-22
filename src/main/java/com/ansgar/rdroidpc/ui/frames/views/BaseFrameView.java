package com.ansgar.rdroidpc.ui.frames.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public interface BaseFrameView {
    void onCloseFrame();

    Rectangle getRectangle();

    void setKeyboardListener(KeyListener listener);

    JComponent getChildComponent();
}
