package com.ansgar.rdroidpc.enums

import com.ansgar.rdroidpc.listeners.MenuItemEnumCommands
import com.ansgar.rdroidpc.ui.frames.views.MainPanelView

enum class MainMenuItemsEnum(val value: String) : MenuItemEnumCommands<MainPanelView> {
    FILE("File"),
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
    HELP("Help"),
    INFORMATION("Information") {
        override fun execute(view: MainPanelView) {
            view.openInformation()
        }
    };

    companion object {
        fun execute(value: String, view: MainPanelView) {
            values().firstOrNull { it.value == value }?.execute(view)
        }
    }

}
