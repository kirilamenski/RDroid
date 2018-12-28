package com.ansgar.rdoidpc.constants

enum class AdbCommandEnum(val command: String) {
    // TODO Make by one shell command
    DEVICE("adb devices; adb shell wm size; adb shell getprop | grep ro.product.model"),

    DEVICES("adb devices"),
    DEVICE_NAME("adb -s %s shell getprop | grep ro.product.model"),
    DEVICE_SCREEN_SIZE("adb -s %s shell wm size"),
    KILL_SERVER("adb kill-server"),
    START_SERVER("adb start-server"),
    SHOW_SCREEN("adb shell screenrecord --bit-rate=4m --output-format=h264 --size 1920x1080 - "),

    /**adb screenrecorder has time limit - 180 s (3 min). To solve this problem and don't get exception limit range you need to use 'hack'.
     * repeat string with screenrecord several time. Video will be streaming line * 180 s.
     */
    SHOW_SCREEN_WITHOUT_TIME("adb -s %s shell screenrecord --bit-rate 64m --output-format=h264 --time-limit 180 --size 1920x1080 - ;" +
            "screenrecord --bit-rate 64m --output-format=h264 --time-limit 180 --size 1920x1080 - ;" +
            "screenrecord --bit-rate 64m --output-format=h264 --time-limit 180 --size 1920x1080 - "),
    ADB_SHELL("adb -s %s shell"),
    SCREEN_RECORD("screenrecord --bit-rate %dm --output-format=h264 --time-limit 180 --size %dx%d - ")
}