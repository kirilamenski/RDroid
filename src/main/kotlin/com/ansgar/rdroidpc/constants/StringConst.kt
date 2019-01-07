package com.ansgar.rdroidpc.constants

class StringConst {

    companion object {
        const val MENU_TITLE_FILE = "File"
        const val MENU_TITLE_HELP = "Help"
        const val MENU_SETTINGS = "Settings"
        var menuItems = arrayOf("File", arrayOf("Settings", "Exit"), "Help")
        val DEVICES_CONTAINER_HEADER_NAMES = arrayOf("name", "size", "device id", "status", "")
    }
}