package com.ansgar.rdroidpc.entities

import kotlin.collections.ArrayList

class DumpsysModel {
    var window: String? = null
    var statsSince: String? = null
    var totalFramesRendered: Int = 0
    var jankyFrames: String? = null
    var numberMissedVsync: Int = 0
    var numberHighInputLatency: Int = 0
    var numberSlowUiThread: Int = 0
    var numberSlowBitmapUploads: Int = 0
    var slowIssueDrawCommands: Int = 0
    var frameDeadLineMissed: Int = 0
    var profileData: ArrayList<ProfileData> = ArrayList()
    var totalViews: Int = 0
    var totalDisplayList: String? = null
    var totalGpuMemoryUsage: String? = null
    var other: ArrayList<String> = ArrayList()

    override fun toString(): String {
        return "DumpsysModel(window=$window," +
                " statsSince=$statsSince," +
                " totalFramesRendered=$totalFramesRendered," +
                " jankyFrames=$jankyFrames," +
                " numberMissedVsync=$numberMissedVsync," +
                " numberHighInputLatency=$numberHighInputLatency," +
                " numberSlowUiThread=$numberSlowUiThread," +
                " numberSlowBitmapUploads=$numberSlowBitmapUploads," +
                " slowIssueDrawCommands=$slowIssueDrawCommands," +
                " frameDeadLineMissed=$frameDeadLineMissed," +
                " profileData=$profileData," +
                " totalViews=$totalViews," +
                " totalDisplayList=$totalDisplayList," +
                " totalGpuMemoryUsage=$totalGpuMemoryUsage," +
                " other=$other)"
    }


}
