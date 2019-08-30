package com.ansgar.rdroidpc.constants

import com.ansgar.rdroidpc.enums.MainMenuItemsEnum
import com.ansgar.rdroidpc.enums.VideoMenuItemsEnum

class StringConst {

    companion object {
        var mainMenuItems = arrayOf(
                MainMenuItemsEnum.FILE.value, arrayOf(MainMenuItemsEnum.SETTINGS.value, MainMenuItemsEnum.EXIT.value),
                MainMenuItemsEnum.HELP.value, arrayOf(MainMenuItemsEnum.INFORMATION.value))
        var videoMenuItems = arrayOf(
                VideoMenuItemsEnum.FILE.value, arrayOf(VideoMenuItemsEnum.SETTINGS.value, VideoMenuItemsEnum.EXIT.value),
                VideoMenuItemsEnum.TOOLS.value, arrayOf(VideoMenuItemsEnum.PERFORMANCE_MANAGER.value),
                VideoMenuItemsEnum.HELP.value)
        val deviceHeaderNames = arrayOf("name", "size", "device id", "status", "")
        val defaultScreenSizes = arrayOf("480x640", "540x960", "720x1280", "1080x1920", "540x1140",
                "720x1520", "1080x2280", "1440x2560")
        val bitRates = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "24", "25", "26", "27", "28", "29", "30",
                "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
                "41", "42", "43", "44", "45", "46", "48", "49", "50")
        val navigationPanelIcons = arrayOf(
                "icons/ic_rotate_device_64.png",
                "icons/ic_screen_capture_64.png",
                "icons/ic_screen_record_64.png",
                "icons/ic_menu_64.png",
                "icons/ic_reconnect_64.png",
                "icons/ic_back_64.png",
                "icons/ic_home_64.png",
                "icons/ic_recent_app_64.png"
        )
        val navigationPanelTooltips = arrayOf(
                "Rotate",
                "Screen Capture",
                "Screen Record",
                "Menu",
                "Reconnect",
                "Back",
                "Home",
                "Recent Application"
        )

        const val INFORMATION = "<html><h2>FEATURES:</h2>" +
                "- control android device from PC.<br>" +
                "- video recording and screen capturing.<br>" +
                "- uploading files by drag and drop.<br>" +
                "<br>" +
                "<h2>HOT KEYS:</h2>" +
                "|alt + left arrow \t|\tgo back\t|<br>" +
                "|ctrl + alt + h\t|\tgo home\t|<br>" +
                "|ctrl + alt + l\t|\topen recent app list\t|<br>" +
                "|ctrl + shift + F10\t|\tmute volume\t|<br>" +
                "|ctrl + shift + F11\t|\tvolume down\t|<br>" +
                "|ctrl + shift + F12\t|\tvolume up\t|<br>" +
                "|left/top/right/bottom arrows|navigation|<br>" +
                "<br>" +
                "<h2>CONTACTS:</h2></html>"

        val links = arrayOf(
                "https://github.com/kirilamenski/RDroid/issues",
                "<html>Visit <a href=\"https://github.com/kirilamenski/RDroid/issues\">https://github.com/kirilamenski/RDroid/issues</a> to report about issues</html>"
        )

        const val BIT_RATE_L = "Bit Rate"
        const val DOWNLOAD_FOLDER = "Download Folder"
        const val TIME_L = "Time"
        const val SCREEN_RESOLUTION_L = "Screen Resolution"
        const val OK = "Ok"
        const val CANCEL = "Cancel"

        const val ASK_REBOOT = "Do you want to reboot device?"

        const val DEFAULT_DEVICE_FOLDER = "/sdcard/"

        const val SCREEN_RECORD_ALREADY_RUNNING = "Screen Record_already_running"
        const val SCREEN_RECORDING_L = "Screen recording %d"
        const val INSTALL_APK_MESSAGE = "Do you want to install this apk?"

        const val RUN = "Run"
    }
}