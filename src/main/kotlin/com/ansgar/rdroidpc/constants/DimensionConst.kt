package com.ansgar.rdroidpc.constants

import java.awt.Rectangle

class DimensionConst {

    companion object {
        const val MAIN_WINDOW_WIDTH = 800
        const val MAIN_WINDOW_HEIGHT = 420
        const val SETTINGS_PANEL_WIDTH = 450
        const val SETTINGS_PANEL_HEIGHT = 300
        const val NAVIGATION_PANEL_HEIGHT = 40
        const val SCREEN_RATIO = 0.5625f
        const val DEFAULT_WIDTH = 720
        const val DEVICE_CONTAINER_HEIGHT = 50

        val mainFrameRect = Rectangle(0, 100, DimensionConst.MAIN_WINDOW_WIDTH, DimensionConst.MAIN_WINDOW_HEIGHT)
        val videoFrameRectangle = Rectangle(0, 0, DEFAULT_WIDTH / 2, ((DEFAULT_WIDTH / SCREEN_RATIO / 2).toInt()))
        val settingFrameRect = Rectangle(0, 0, DimensionConst.SETTINGS_PANEL_WIDTH, DimensionConst.SETTINGS_PANEL_HEIGHT)
    }

}