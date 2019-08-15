package com.ansgar.rdroidpc.enums

enum class VideoMenuItemsEnum(val value: String) {
    FILE("File"),
    SETTINGS("Settings"),
    TOOLS("Tools"),
    PERFORMANCE_MANAGER("Performance Manager"),
    EXIT("Exit"),
    HELP("Help");

    companion object {
        fun getMenuItemEnumByValue(value: String): VideoMenuItemsEnum? = values().firstOrNull { menuItemsEnum ->
            menuItemsEnum.value == value
        }
    }
}
