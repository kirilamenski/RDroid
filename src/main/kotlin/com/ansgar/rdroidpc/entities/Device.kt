package com.ansgar.rdroidpc.entities

import java.io.Serializable

open class Device(
        var deviceId: String? = null,
        var deviceStatus: String? = null,
        var deviceName: String? = null,
        var width: Int = 0,
        var height: Int = 0,
        var option: Option? = null
) : Serializable