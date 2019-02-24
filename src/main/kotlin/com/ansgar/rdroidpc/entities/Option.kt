package com.ansgar.rdroidpc.entities

import java.io.Serializable

class Option(
        var bitRate: Int = 4,
        var width: Int = 720,
        var height: Int = 1280) : Serializable