package com.ansgar.rdroidpc.entities

class Option(
        var bitRate: Int = 4,
        var width: Int = 720,
        var height: Int = 1280) {

    override fun toString(): String = "[" +
            " bitRate: $bitRate," +
            " width: $width," +
            " height: $height" +
            "]"

}