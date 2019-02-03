package com.ansgar.rdroidpc.entities

class DeviceOption(
        var bitRate: Int = 4,
        var width: Int = 720,
        var height: Int = 1280) {

    override fun toString(): String = "[" +
            " bitRate: $bitRate," +
            " width: $width," +
            " height: $height" +
            "]"

}