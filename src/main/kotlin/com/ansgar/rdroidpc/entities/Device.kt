package com.ansgar.rdroidpc.entities

import org.jetbrains.annotations.NotNull
import java.io.Serializable

open class Device(
        @NotNull
        var deviceId: String = "",
        var deviceStatus: String? = null,
        var deviceName: String? = null,
        var width: Int = 0,
        var height: Int = 0,
        var option: Option = Option()
) : Serializable