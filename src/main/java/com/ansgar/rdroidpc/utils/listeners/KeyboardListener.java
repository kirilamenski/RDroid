package com.ansgar.rdroidpc.utils.listeners;

import com.android.chimpchat.core.TouchPressType;
import com.ansgar.rdoidpc.constants.AdbKeyCode;
import com.ansgar.rdroidpc.ui.frames.VideoFrame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {

    private VideoFrame frame;

    public KeyboardListener(VideoFrame frame) {
        this.frame = frame;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        AdbKeyCode adbKeyCode = AdbKeyCode.Companion.getAdbKeyEvent(e);
        if (adbKeyCode != AdbKeyCode.KEYCODE_UNKNOWN && adbKeyCode.isChar()) {
            frame.getChimpDevice().type(String.valueOf(e.getKeyChar()));
        } else {
            frame.getChimpDevice().press(String.valueOf(adbKeyCode.getKeyCode()), TouchPressType.DOWN_AND_UP);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
