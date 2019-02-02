package com.ansgar.rdroidpc.constants

class StringConst {

    companion object {
        var menuItems = arrayOf(
                MenuItemsEnum.FILE.value, arrayOf(MenuItemsEnum.SETTINGS.value, MenuItemsEnum.EXIT.value),
                MenuItemsEnum.HELP.value)
        val DEVICES_CONTAINER_HEADER_NAMES = arrayOf("name", "size", "device id", "status", "")
        val SCREEN_RESOLUTION_ARRAY_LIST = arrayOf("540x960", "720x1280", "1080x1920", "1440x24560")
        const val BIT_RATE_L = "Bit Rate"
        const val SCREEN_RESOLUTION_L = "Screen Resolution"
        const val OK = "Ok"
        const val CANCEL = "Cancel"
        const val DEVICE_OPTIONS = "Device Options"
    }
}