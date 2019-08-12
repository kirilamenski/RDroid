package com.ansgar.rdroidpc.entities

import java.util.*

class DumpsysModel {
    var window: String? = null
    var statsSince: String? = null
    var totalFramesRendered: Int? = null
    var jankyFrames: Int? = null
    var numberMissedVsync: Int? = null
    var numberHighInputLatency: Int? = null
    var numberSlowUiThread: Int? = null
    var numberSlowBitmapUploads: Int? = null
    var slowissueDrawCommands: Int? = null
    var frameDeadLineMissed: Int? = null
    //TODO LinkedList should be more faster. Lets check
    var profileDatas: LinkedList<ProfileData>? = null
}