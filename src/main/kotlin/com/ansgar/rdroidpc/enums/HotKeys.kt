package com.ansgar.rdroidpc.enums

import com.android.chimpchat.core.TouchPressType
import com.ansgar.rdroidpc.constants.AdbKeyCode
import com.ansgar.rdroidpc.listeners.OnDeviceInputListener
import com.ansgar.rdroidpc.utils.ToolkitUtils
import org.jetbrains.annotations.NotNull
import java.awt.event.KeyEvent

enum class HotKeys(val keyCode: AdbKeyCode?) : HotKeysActions {
    // TODO can be modified to get HashSet directly when file with saved hot keys will be added

    BACK(AdbKeyCode.KEYCODE_BACK) {
        override fun execute(@NotNull listener: OnDeviceInputListener) {
            listener.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
        }

        override fun getKeyCodes(): HashSet<Int>? {
            val set = HashSet<Int>()
            set.add(KeyEvent.VK_ALT)
            set.add(KeyEvent.VK_LEFT)
            return set
        }
    },
    HOME(AdbKeyCode.KEYCODE_HOME) {
        override fun execute(@NotNull listener: OnDeviceInputListener) {
            listener.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
        }

        override fun getKeyCodes(): HashSet<Int>? {
            val set = HashSet<Int>()
            set.add(KeyEvent.VK_CONTROL)
            set.add(KeyEvent.VK_ALT)
            set.add(KeyEvent.VK_H)
            return set
        }
    },
    RECENT_APP(AdbKeyCode.KEYCODE_APP_SWITCH) {
        override fun execute(@NotNull listener: OnDeviceInputListener) {
            listener.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
        }

        override fun getKeyCodes(): HashSet<Int>? {
            val set = HashSet<Int>()
            set.add(KeyEvent.VK_CONTROL)
            set.add(KeyEvent.VK_ALT)
            set.add(KeyEvent.VK_L)
            return set
        }
    },
    VOLUME_UP(AdbKeyCode.KEYCODE_VOLUME_UP) {
        override fun execute(@NotNull listener: OnDeviceInputListener) {
            listener.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
        }

        override fun getKeyCodes(): HashSet<Int>? {
            val set = HashSet<Int>()
            set.add(KeyEvent.VK_CONTROL)
            set.add(KeyEvent.VK_SHIFT)
            set.add(KeyEvent.VK_F12)
            return set
        }
    },
    VOLUME_DOWN(AdbKeyCode.KEYCODE_VOLUME_DOWN) {
        override fun execute(@NotNull listener: OnDeviceInputListener) {
            listener.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
        }

        override fun getKeyCodes(): HashSet<Int>? {
            val set = HashSet<Int>()
            set.add(KeyEvent.VK_CONTROL)
            set.add(KeyEvent.VK_SHIFT)
            set.add(KeyEvent.VK_F11)
            return set
        }
    },
    VOLUME_MUTE(AdbKeyCode.KEYCODE_VOLUME_MUTE) {
        override fun execute(@NotNull listener: OnDeviceInputListener) {
            listener.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
        }

        override fun getKeyCodes(): HashSet<Int>? {
            val set = HashSet<Int>()
            set.add(KeyEvent.VK_CONTROL)
            set.add(KeyEvent.VK_SHIFT)
            set.add(KeyEvent.VK_F10)
            return set
        }
    },
    PC_PASTE(null) {
        override fun execute(@NotNull listener: OnDeviceInputListener) {
            listener.type(ToolkitUtils.getTextFromClipboard())
        }

        override fun getKeyCodes(): HashSet<Int>? {
            val set = HashSet<Int>()
            set.add(KeyEvent.VK_CONTROL)
            set.add(KeyEvent.VK_SHIFT)
            set.add(KeyEvent.VK_V)
            return set
        }
    },
    DEVICE_COPY(AdbKeyCode.KEYCODE_COPY) {
        override fun execute(@NotNull listener: OnDeviceInputListener) {
            listener.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
        }

        override fun getKeyCodes(): HashSet<Int>? {
            val set = HashSet<Int>()
            set.add(KeyEvent.VK_CONTROL)
            set.add(KeyEvent.VK_C)
            return set
        }
    },
    DEVICE_PASTE(AdbKeyCode.KEYCODE_PASTE) {
        override fun execute(@NotNull listener: OnDeviceInputListener) {
            listener.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
        }

        override fun getKeyCodes(): HashSet<Int>? {
            val set = HashSet<Int>()
            set.add(KeyEvent.VK_CONTROL)
            set.add(KeyEvent.VK_V)
            return set
        }
    },
    LANGUAGE_SWITCH(AdbKeyCode.KEYCODE_LANGUAGE_SWITCH) {
        override fun execute(@NotNull listener: OnDeviceInputListener) {
            listener.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
        }

        override fun getKeyCodes(): HashSet<Int>? {
            val set = HashSet<Int>()
            set.add(KeyEvent.VK_CONTROL)
            set.add(KeyEvent.VK_ALT)
            set.add(KeyEvent.VK_SHIFT)
            return set
        }
    };

    companion object {

        fun execute(hotKey: HotKeys, @NotNull listener: OnDeviceInputListener) {
            listener.press(hotKey.keyCode.toString(), TouchPressType.DOWN_AND_UP)
        }

        fun execute(pressedKeys: HashSet<Int>, @NotNull listener: OnDeviceInputListener) {
            for (hotKey in values()) {
                val keyCodes = hotKey.getKeyCodes()
                if (keyCodes != null
                        && keyCodes.size == pressedKeys.size &&
                        pressedKeys.containsAll(keyCodes)) {
                    hotKey.execute(listener)
                    break
                }
            }
        }

    }

}

interface HotKeysActions {
    fun getKeyCodes(): HashSet<Int>?

    fun execute(@NotNull listener: OnDeviceInputListener)
}