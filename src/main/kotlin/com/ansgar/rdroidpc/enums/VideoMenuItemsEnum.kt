package com.ansgar.rdroidpc.enums

import com.ansgar.rdroidpc.listeners.MenuItemEnumCommands
import com.ansgar.rdroidpc.ui.frames.views.VideoFrameView

enum class VideoMenuItemsEnum(val value: String): MenuItemEnumCommands<VideoFrameView> {
    FILE("File") {
        override fun execute(view: VideoFrameView) {

        }
    },
    SETTINGS("Settings") {
        override fun execute(view: VideoFrameView) {

        }
    },
    TOOLS("Tools") {
        override fun execute(view: VideoFrameView) {

        }
    },
    PERFORMANCE_MANAGER("Performance Manager") {
        override fun execute(view: VideoFrameView) {

        }
    },
    EXIT("Exit") {
        override fun execute(view: VideoFrameView) {
            view.onCloseFrame()
        }
    },
    HELP("Help") {
        override fun execute(view: VideoFrameView) {

        }
    };

    companion object {
        fun execute(value: String, view: VideoFrameView) {
            values().filter { it.value == value }.firstOrNull()?.execute(view)
        }
    }
}
