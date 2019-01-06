package com.ansgar.rdroidpc.entities

class DeviceOption(
        var deviceId: String? = null,
        var deviceName: String? = null,
        var bitRate: Int = 4,
        var width: Int = 720,
        var height: Int = 1280) {

    override fun toString(): String = "[" +
            "deviceId: $deviceId, " +
            "deviceName: $deviceName," +
            " bitRate: $bitRate," +
            " width: $width," +
            " height: $height" +
            "]"

}