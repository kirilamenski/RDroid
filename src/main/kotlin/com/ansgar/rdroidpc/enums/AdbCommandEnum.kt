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
    INSTALL_APK("adb -s %s install -r %s"),
    ROTATE_DEVICE("adb -s %s shell settings put system user_rotation %d"),
    TAKE_SNAPSHOT("adb -s %s exec-out screencap -p > %s"),
    ADB_TAKE_SNAPSHOT("adb -s %s shell screencap -p %s"),
    ADB_PULL_SNAPSHOT("adb -s %s pull %s %s"),
    ADB_REMOVE_FILE("adb -s %s shell rm -f %s"),
    SCREEN_STREAMING("screenrecord --bit-rate %dm --output-format=h264 --time-limit 180 --size %dx%d - "),
    SCREEN_RECORD("adb -s %s shell screenrecord --bit-rate %dm --time-limit %d --size %dx%d %s"),
    UPLOAD_FILES("adb -s %s push %s %s"),
    KEY_EVENT("adb -s %s shell input keyevent %s"),
    DUMPSYS("adb -s %s shell dumpsys gfxinfo %s framestats"),
    INPUT_TEXT("adb -s %s shell input text \"%s\""),
    PACKAGE_INFO("adb -s %s shell dumpsys package %s"),
    PACKAGES("adb -s %s shell pm list packages"),
    CLEAR_APP_DATA("adb -s %s shell pm clear %s"),
    UN_INSTALL_APP("adb -s %s uninstall %s"),
    OPEN_APP("adb -s %s shell monkey -p %s -c android.intent.category.LAUNCHER 1");

    companion object {
        fun getCommandValue(commandEnum: AdbCommandEnum): String {
            val aheadPath: String = SharedValues.get(SharedValuesKey.ADB_PATH, "")
                    .replace("adb", "")
            return "$aheadPath${commandEnum.command}"
        }
    }

}