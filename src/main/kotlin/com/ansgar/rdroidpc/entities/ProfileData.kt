package com.ansgar.rdroidpc.entities

class ProfileData {
    var flags: Int? = null
    var intendedVsync: Long? = null
    var vsync: Long? = null
    var oldestInputEvent: Long? = null
    var newestInputEvent: Long? = null
    var handleInputStart: Long? = null
    var aniationStart: Long? = null
    var performTraverSalsStart: Long? = null
    var drawStart: Long? = null
    var syncQueued: Long? = null
    var syncStart: Long? = null
    var issueDrawCommandsStart: Long? = null
    var swapBuffers: Long? = null
    var frameCompleted: Long? = null
}