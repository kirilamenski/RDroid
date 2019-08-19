package com.ansgar.rdroidpc.listeners;

import com.android.chimpchat.core.TouchPressType;
import com.ansgar.rdroidpc.constants.AdbKeyCode;
import com.ansgar.rdroidpc.enums.HotKeys;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class KeyboardListener implements KeyListener {

    private HashSet<Integer> pressedKey = new HashSet<>();
    private OnDeviceInputListener listener;

    public KeyboardListener(OnDeviceInputListener listener) {
        this.listener = listener;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (listener != null) {
            pressedKey.add(e.getKeyCode());

            if (pressedKey.size() > 1 && !(pressedKey.size() == 2 && pressedKey.contains(KeyEvent.VK_SHIFT))) {
                executeHotKey(listener);
                return;
            }

            AdbKeyCode adbKeyCode = AdbKeyCode.Companion.getAdbKeyEvent(e);

            if (adbKeyCode != AdbKeyCode.KEYCODE_UNKNOWN && adbKeyCode.isChar()) {
                listener.type(String.valueOf(e.getKeyChar()));
            } else {
                listener.press(String.valueOf(adbKeyCode.getKeyCode()), TouchPressType.DOWN_AND_UP);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKey.remove(e.getKeyCode());
    }

    private void executeHotKey(@NotNull OnDeviceInputListener listener) {
        HotKeys.Companion.execute(pressedKey, listener);
    }

}
