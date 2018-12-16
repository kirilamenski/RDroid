package com.ansgar.rdoidpc.entities

open class Device(var deviceId: String? = null,
                  var deviceStatus: String? = null,
                  var deviceName: String? = null,
                  var width: Int = 0,
                  var height: Int = 0) {

    override fun toString(): String {
        return "Device(deviceId=$deviceId, deviceStatus=$deviceStatus, deviceName=$deviceName, width=$width, height=$height)"
    }

}