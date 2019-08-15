package com.ansgar.rdroidpc.enums

enum class MainMenuItemsEnum(val value: String) {
    FILE("File"),
    SETTINGS("Settings"),
    EXIT("Exit"),
    HELP("Help"),
    INFORMATION("Information");

    companion object {
        fun getMenuItemEnumByValue(value: String): MainMenuItemsEnum? = values().firstOrNull { menuItemsEnum ->
            menuItemsEnum.value == value
        }
    }

}
