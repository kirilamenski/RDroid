package com.ansgar.rdroidpc.listeners

import com.ansgar.rdroidpc.ui.frames.views.BaseFrameView

interface MenuItemEnumCommands<T : BaseFrameView> {
    fun execute(view: T)
}