package com.ansgar.rdroidpc.enums

import com.ansgar.rdroidpc.constants.SharedValuesKey
import com.ansgar.rdroidpc.utils.SharedValues

enum class AdbCommandEnum(val command: String) {
    // TODO Make by one shell command
    DEVICE("adb devices; adb shell wm size; adb shell getprop | grep ro.product.model"),

    ADB_SHELL("adb -s %s shell"),
    DEVICES("adb devices"),
    DEVICE_NAME("adb -s %s shell getprop | grep ro.product.model"),
    DEVICE_SCREEN_SIZE("adb -s %s shell wm size"),
    KILL_SERVER("adb kill-server"),
    START_SERVER("adb start-server"),
    ORIENTATION("adb -s %s shell dumpsys input | grep 'SurfaceOrientation'"),
    ACCELEROMETER_ENABLE("adb -s %s shell settings put system accelerometer_rotation %d"),
    ROTATE_DEVICE("adb -s %s shell settings put system user_rotation %d"),
    TAKE_SNAPSHOT("adb -s %s exec-out screencap -p > %s"),
    ADB_TAKE_SNAPSHOT("adb -s %s shell screencap -p %s"),
    ADB_PULL_SNAPSHOT("adb -s %s pull %s %s"),
    ADB_REMOVE_FILE("adb -s %s shell rm -f %s"),
    SHOW_SCREEN("adb shell screenrecord --bit-rate=4m --output-format=h264 --size 1920x1080 - "),
    /**adb screenrecorder has time limit - 180 s (3 min). To solve this problem and don't get exception limit range you need to use 'hack'.
     * repeat string with screenrecord several time. Video will be streaming line * 180 s.
     */
    SHOW_SCREEN_WITHOUT_TIME("adb -s %s shell screenrecord --bit-rate 64m --output-format=h264 --time-limit 180 --size 1920x1080 - ;" +
            "screenrecord --bit-rate 64m --output-format=h264 --time-limit 180 --size 1920x1080 - ;" +
            "screenrecord --bit-rate 64m --output-format=h264 --time-limit 180 --size 1920x1080 - "),
    SCREEN_RECORD("screenrecord --bit-rate %dm --output-format=h264 --time-limit 180 --size %dx%d - "),
    UPLOAD_FILES("adb -s %s push %s %s");

    companion object {
        fun getCommandValue(commandEnum: AdbCommandEnum): String {
            val aheadPath: String = SharedValues.get(SharedValuesKey.ADB_PATH, "")
                    .replace("adb", "")
            values().forEach { enum ->
                if (commandEnum == enum) return "$aheadPath${enum.command}"
            }
            return ""
        }
    }

}