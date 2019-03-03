package com.ansgar.rdroidpc.constants

import com.ansgar.rdroidpc.enums.MenuItemsEnum

class StringConst {

    companion object {
        var menuItems = arrayOf(
                MenuItemsEnum.FILE.value, arrayOf(MenuItemsEnum.SETTINGS.value, MenuItemsEnum.EXIT.value),
                MenuItemsEnum.HELP.value)
        val deviceHeaderNames = arrayOf("name", "size", "device id", "status", "")
        val defaultScreenSizes = arrayOf("480x640", "540x960", "720x1280", "1080x1920", "1440x2560")
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

        const val BIT_RATE_L = "Bit Rate"
        const val DOWNLOAD_FOLDER = "Download Folder"
        const val TIME_L = "Time"
        const val SCREEN_RESOLUTION_L = "Screen Resolution"
        const val OK = "Ok"
        const val CANCEL = "Cancel"

        const val SHARED_VAL_SCREEN_WIDTH = "shared_val_screen_width"
        const val SHARED_VAL_SCREEN_HEIGHT = "shared_val_screen_height"

        const val ASK_REBOOT = "Do you want to reboot device?"

        const val DEFAULT_DEVICE_FOLDER = "/sdcard/"
    }
}