package com.ansgar.rdroidpc.enums

enum class MenuItemsEnum(val value: String) {
    FILE("File"),
    SETTINGS("Settings"),
    EXIT("Exit"),
    HELP("Help"),
    REPORT("Report an issue"),
    CONTACT("Contact me");

    companion object {
        fun getMenuItemEnumByValue(value: String): MenuItemsEnum? = values().firstOrNull { menuItemsEnum ->
            menuItemsEnum.value == value
        }
    }

}
