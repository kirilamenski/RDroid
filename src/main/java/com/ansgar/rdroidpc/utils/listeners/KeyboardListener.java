package com.ansgar.rdroidpc.utils.listeners;

import com.android.chimpchat.core.TouchPressType;
import com.ansgar.rdoidpc.constants.AdbKeyCode;
import com.ansgar.rdroidpc.ui.frames.VideoFrame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class KeyboardListener implements KeyListener {

    private VideoFrame frame;
    private HashMap<Integer, Integer> pressedKey = new HashMap();

    public KeyboardListener(VideoFrame frame) {
        this.frame = frame;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKey.put(e.getKeyCode(), e.getKeyCode());
        if (pressedKey.size() > 1) {
            checkForMultipleKey();
            return;
        }

        AdbKeyCode adbKeyCode = AdbKeyCode.Companion.getAdbKeyEvent(e);
        if (adbKeyCode != AdbKeyCode.KEYCODE_UNKNOWN && adbKeyCode.isChar()) {
            frame.getChimpDevice().type(String.valueOf(e.getKeyChar()));
        } else {
            frame.getChimpDevice().press(String.valueOf(adbKeyCode.getKeyCode()), TouchPressType.DOWN_AND_UP);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKey.remove(e.getKeyCode());
    }

    private void checkForMultipleKey() {
        AdbKeyCode adbKeyCode = AdbKeyCode.KEYCODE_UNKNOWN;
        if (pressedKey.containsKey(KeyEvent.VK_ALT) && pressedKey.containsKey(KeyEvent.VK_LEFT)) {
            adbKeyCode = AdbKeyCode.KEYCODE_BACK;
        } else if (pressedKey.containsKey(KeyEvent.VK_CONTROL) && pressedKey.containsKey(KeyEvent.VK_SHIFT) && pressedKey.containsKey(KeyEvent.VK_H)) {
            adbKeyCode = AdbKeyCode.KEYCODE_HOME;
        } else if (pressedKey.containsKey(KeyEvent.VK_CONTROL) && pressedKey.containsKey(KeyEvent.VK_SHIFT) && pressedKey.containsKey(KeyEvent.VK_L)) {
            adbKeyCode = AdbKeyCode.KEYCODE_APP_SWITCH;
        } else if (pressedKey.containsKey(KeyEvent.VK_CONTROL) && pressedKey.containsKey(KeyEvent.VK_SHIFT) && pressedKey.containsKey(KeyEvent.VK_F12)) {
            adbKeyCode = AdbKeyCode.KEYCODE_VOLUME_UP;
        } else if (pressedKey.containsKey(KeyEvent.VK_CONTROL) && pressedKey.containsKey(KeyEvent.VK_SHIFT) && pressedKey.containsKey(KeyEvent.VK_F11)) {
            adbKeyCode = AdbKeyCode.KEYCODE_VOLUME_DOWN;
        } else if (pressedKey.containsKey(KeyEvent.VK_CONTROL) && pressedKey.containsKey(KeyEvent.VK_SHIFT) && pressedKey.containsKey(KeyEvent.VK_F10)) {
            adbKeyCode = AdbKeyCode.KEYCODE_VOLUME_MUTE;
        }
        if (adbKeyCode != AdbKeyCode.KEYCODE_UNKNOWN) {
            frame.getChimpDevice().press(String.valueOf(adbKeyCode.getKeyCode()), TouchPressType.DOWN_AND_UP);
        }
    }

}
