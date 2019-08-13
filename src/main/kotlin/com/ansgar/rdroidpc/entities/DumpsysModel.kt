package com.ansgar.rdroidpc.entities

import java.util.*

class DumpsysModel {
    var window: String? = null
    var statsSince: String? = null
    var totalFramesRendered: Int = 0
    var jankyFrames: Int = 0
    var numberMissedVsync: Int = 0
    var numberHighInputLatency: Int = 0
    var numberSlowUiThread: Int = 0
    var numberSlowBitmapUploads: Int = 0
    var slowissueDrawCommands: Int = 0
    var frameDeadLineMissed: Int = 0
    //TODO LinkedList should be more faster. Lets check
    var profileDatas: LinkedList<ProfileData>? = null
}