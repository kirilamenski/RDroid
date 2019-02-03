package com.ansgar.rdroidpc.entities

open class Device(
        var deviceId: String? = null,
        var deviceStatus: String? = null,
        var deviceName: String? = null,
        var width: Int = 0,
        var height: Int = 0,
        var option: DeviceOption? = null
) {

    override fun toString(): String = "[" +
            "deviceId: $deviceId, " +
            "deviceStatus: $deviceStatus, " +
            "deviceName: $deviceName, " +
            "width: $width, " +
            "height: $height" +
            "option: ${option?.toString()}" +
            "]"

}