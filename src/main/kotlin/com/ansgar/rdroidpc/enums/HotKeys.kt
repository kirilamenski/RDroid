package com.ansgar.rdroidpc.enums

import com.android.chimpchat.core.IChimpDevice
import com.android.chimpchat.core.TouchPressType
import com.ansgar.rdroidpc.constants.AdbKeyCode
import com.ansgar.rdroidpc.utils.ToolkitUtils
import java.awt.event.KeyEvent

enum class HotKeys(val keyCode: AdbKeyCode?) : HotKeysActions {
    // TODO can be modified to get HashSet directly when file with saved hot keys will be added

    BACK(AdbKeyCode.KEYCODE_BACK) {
        override fun execute(chimpDevice: IChimpDevice) {
            chimpDevice.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
        }

        override fun getKeyCodes(): HashSet<Int>? {
            val set = HashSet<Int>()
            set.add(KeyEvent.VK_ALT)
            set.add(KeyEvent.VK_LEFT)
            return set
        }
    },
    HOME(AdbKeyCode.KEYCODE_HOME) {
        override fun execute(chimpDevice: IChimpDevice) {
            chimpDevice.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
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
        override fun execute(chimpDevice: IChimpDevice) {
            chimpDevice.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
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
        override fun execute(chimpDevice: IChimpDevice) {
            chimpDevice.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
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
        override fun execute(chimpDevice: IChimpDevice) {
            chimpDevice.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
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
        override fun execute(chimpDevice: IChimpDevice) {
            chimpDevice.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
        }

        override fun getKeyCodes(): HashSet<Int>? {
            val set = HashSet<Int>()
            set.add(KeyEvent.VK_CONTROL)
            set.add(KeyEvent.VK_SHIFT)
            set.add(KeyEvent.VK_F10)
            return set
        }
    },
    PC_COPY(null) {
        override fun execute(chimpDevice: IChimpDevice) {
            chimpDevice.type(ToolkitUtils.getTextFromClipboard())
        }

        override fun getKeyCodes(): HashSet<Int>? {
            val set = HashSet<Int>()
            set.add(KeyEvent.VK_CONTROL)
            set.add(KeyEvent.VK_SHIFT)
            set.add(KeyEvent.VK_C)
            return set
        }
    },
    DEVICE_COPY(AdbKeyCode.KEYCODE_COPY) {
        override fun execute(chimpDevice: IChimpDevice) {
            chimpDevice.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
        }

        override fun getKeyCodes(): HashSet<Int>? {
            val set = HashSet<Int>()
            set.add(KeyEvent.VK_CONTROL)
            set.add(KeyEvent.VK_C)
            return set
        }
    },
    DEVICE_PASTE(AdbKeyCode.KEYCODE_PASTE) {
        override fun execute(chimpDevice: IChimpDevice) {
            chimpDevice.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
        }

        override fun getKeyCodes(): HashSet<Int>? {
            val set = HashSet<Int>()
            set.add(KeyEvent.VK_CONTROL)
            set.add(KeyEvent.VK_V)
            return set
        }
    },
    LANGUAGE_SWITCH(AdbKeyCode.KEYCODE_LANGUAGE_SWITCH) {
        override fun execute(chimpDevice: IChimpDevice) {
            chimpDevice.press(keyCode.toString(), TouchPressType.DOWN_AND_UP)
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

        fun execute(hotKey: HotKeys, chimpDevice: IChimpDevice) {
            chimpDevice.press(hotKey.keyCode.toString(), TouchPressType.DOWN_AND_UP)
        }

        fun execute(pressedKeys: HashSet<Int>, chimpDevice: IChimpDevice) {
            for (hotKey in values()) {
                val keyCodes = hotKey.getKeyCodes()
                if (keyCodes != null && pressedKeys.containsAll(keyCodes)) {
                    hotKey.execute(chimpDevice)
                    break
                }
            }
        }

    }

}

interface HotKeysActions {
    fun getKeyCodes(): HashSet<Int>?

    fun execute(chimpDevice: IChimpDevice)
}