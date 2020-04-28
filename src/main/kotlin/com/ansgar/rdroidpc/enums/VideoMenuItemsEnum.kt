package com.ansgar.rdroidpc.enums

import com.ansgar.rdroidpc.listeners.MenuItemEnumCommands
import com.ansgar.rdroidpc.ui.frames.views.VideoFrameView

enum class VideoMenuItemsEnum(val value: String) : MenuItemEnumCommands<VideoFrameView> {
    FILE("File"),
    SETTINGS("Settings"),
    TOOLS("Tools"),
    PERFORMANCE_MANAGER("Performance Manager") {
        override fun execute(view: VideoFrameView) {
            view.openPanel(this)
        }
    },
    PACKAGE_MANAGER("Package Manger") {
        override fun execute(view: VideoFrameView) {
            view.openPanel(this)
        }
    },
    EXIT("Exit") {
        override fun execute(view: VideoFrameView) {
            view.onCloseFrame()
        }
    },
    HELP("Help");

    companion object {
        fun execute(value: String, view: VideoFrameView) {
            values().firstOrNull { it.value == value }?.execute(view)
        }
    }
}
