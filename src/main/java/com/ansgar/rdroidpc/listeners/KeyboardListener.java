package com.ansgar.rdroidpc.listeners;

import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.TouchPressType;
import com.ansgar.rdroidpc.constants.AdbKeyCode;
import com.ansgar.rdroidpc.enums.HotKeys;
import com.ansgar.rdroidpc.ui.frames.VideoFrame;
import com.ansgar.rdroidpc.utils.ToolkitUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class KeyboardListener implements KeyListener {

    private VideoFrame frame;
    private HashSet<Integer> pressedKey = new HashSet<>();

    public KeyboardListener(VideoFrame frame) {
        this.frame = frame;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        IChimpDevice chimpDevice = frame.getChimpDevice();
        if (chimpDevice != null) {
            pressedKey.add(e.getKeyCode());

            if (pressedKey.size() > 1 && !(pressedKey.size() == 2 && pressedKey.contains(KeyEvent.VK_SHIFT))) {
                executeHotKey(chimpDevice);
                return;
            }

            AdbKeyCode adbKeyCode = AdbKeyCode.Companion.getAdbKeyEvent(e);

            if (adbKeyCode != AdbKeyCode.KEYCODE_UNKNOWN && adbKeyCode.isChar()) {
                chimpDevice.type(String.valueOf(e.getKeyChar()));
            } else {
                chimpDevice.press(String.valueOf(adbKeyCode.getKeyCode()), TouchPressType.DOWN_AND_UP);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKey.remove(e.getKeyCode());
    }

    private void executeHotKey(@NotNull IChimpDevice chimpDevice) {
        HotKeys.Companion.execute(pressedKey, chimpDevice);
    }

}
