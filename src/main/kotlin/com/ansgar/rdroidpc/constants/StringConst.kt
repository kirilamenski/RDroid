package com.ansgar.rdroidpc.constants

class StringConst {

    companion object {
        var menuItems = arrayOf(
                MenuItemsEnum.FILE.value, arrayOf(MenuItemsEnum.SETTINGS.value, MenuItemsEnum.EXIT.value),
                MenuItemsEnum.HELP.value)
        val DEVICES_CONTAINER_HEADER_NAMES = arrayOf("name", "size", "device id", "status", "")
        const val OK = "Ok"
    }
}