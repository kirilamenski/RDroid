package com.ansgar.rdroidpc.entities

import kotlin.collections.ArrayList

class DumpsysModel {
    var window: String? = null
    var statsSince: String? = null
    var totalFramesRendered: Int = 0
    var jankyFrames: Int = 0
    var numberMissedVsync: Int = 0
    var numberHighInputLatency: Int = 0
    var numberSlowUiThread: Int = 0
    var numberSlowBitmapUploads: Int = 0
    var slowIssueDrawCommands: Int = 0
    var frameDeadLineMissed: Int = 0
    var profileData: ArrayList<ProfileData> = ArrayList()
    var totlaViews: Int = 0
    var totalDisplayList: String? = null
    var totalGpuMemoryUsage: String? = null
}
