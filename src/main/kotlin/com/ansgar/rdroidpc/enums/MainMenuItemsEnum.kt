package com.ansgar.rdroidpc.enums

import com.ansgar.rdroidpc.ui.frames.views.MainPanelView

enum class MainMenuItemsEnum(val value: String) : MenuItemEnumCommands {
    FILE("File") {
        override fun execute(view: MainPanelView) {

        }
    },
    SETTINGS("Settings") {
        override fun execute(view: MainPanelView) {
            view.openSettings()
        }
    },
    EXIT("Exit") {
        override fun execute(view: MainPanelView) {
            view.onCloseFrame()
        }
    },
    HELP("Help") {
        override fun execute(view: MainPanelView) {
        }
    },
    INFORMATION("Information") {
        override fun execute(view: MainPanelView) {
            view.openInformation()
        }
    };

    companion object {
        fun execute(value: String, view: MainPanelView) {
            values().filter { it.value == value }.firstOrNull()?.execute(view)
        }
    }


}

interface MenuItemEnumCommands {
    fun execute(view: MainPanelView)
}
