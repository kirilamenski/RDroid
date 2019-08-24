package com.ansgar.rdroidpc.entities

class ProfileData {
    var flags: Int = 0
    var intendedVsync: Long = 0
    var vsync: Long = 0
    var oldestInputEvent: Long = 0
    var newestInputEvent: Long = 0
    var handleInputStart: Long = 0
    var animationStart: Long = 0
    var performTraverSalsStart: Long = 0
    var drawStart: Long = 0
    var syncQueued: Long = 0
    var syncStart: Long = 0
    var issueDrawCommandsStart: Long = 0
    var swapBuffers: Long = 0
    var frameCompleted: Long = 0

    override fun toString(): String {
        return "ProfileData(flags=$flags," +
                " intendedVsync=$intendedVsync," +
                " vsync=$vsync," +
                " oldestInputEvent=$oldestInputEvent," +
                " newestInputEvent=$newestInputEvent," +
                " handleInputStart=$handleInputStart," +
                " animationStart=$animationStart," +
                " performTraverSalsStart=$performTraverSalsStart," +
                " drawStart=$drawStart," +
                " syncQueued=$syncQueued," +
                " syncStart=$syncStart," +
                " issueDrawCommandsStart=$issueDrawCommandsStart," +
                " swapBuffers=$swapBuffers," +
                " frameCompleted=$frameCompleted)"
    }


}