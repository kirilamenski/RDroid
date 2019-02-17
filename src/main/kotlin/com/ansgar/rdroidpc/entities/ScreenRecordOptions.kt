package com.ansgar.rdroidpc.entities

import com.ansgar.rdroidpc.constants.DimensionConst

class ScreenRecordOptions(
        var width: Int = DimensionConst.DEFAULT_WIDTH,
        var height: Int = DimensionConst.DEFAULT_HEIGHT,
        var bitRate: Int = 4,
        var time: Int = 180,
        var downloadFolder: String? = null
)