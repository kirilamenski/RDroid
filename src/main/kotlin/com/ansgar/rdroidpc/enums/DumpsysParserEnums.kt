package com.ansgar.rdroidpc.enums

import com.ansgar.rdroidpc.entities.DumpsysModel
import com.ansgar.rdroidpc.entities.ProfileData
import java.lang.StringBuilder

enum class DumpsysParserEnums(var key: String) : DumpsysEnumsActions {
    WINDOW("Window") {
        override fun parse(model: DumpsysModel, line: String) {

        }
    },
    STATUS_SINCE("Stats since") {
        override fun parse(model: DumpsysModel, line: String) {

        }
    },
    TOTAL_FRAMES_RENDERED("Total frames rendered") {
        override fun parse(model: DumpsysModel, line: String) {

        }
    },
    JANKY_FRAMES("Janky frames") {
        override fun parse(model: DumpsysModel, line: String) {

        }
    },
    NUMBER_MISSED_VSYNC("Number Missed Vsync") {
        override fun parse(model: DumpsysModel, line: String) {

        }
    },
    NUMBER_HIGHT_INPUT_LATENCY("Number High input latency") {
        override fun parse(model: DumpsysModel, line: String) {

        }
    },
    NUMBER_SLOW_UI_THREAD("Number Slow UI thread") {
        override fun parse(model: DumpsysModel, line: String) {

        }
    },
    NUMBER_SLOW_BITMAP_UPLOADS("Number Slow bitmap uploads") {
        override fun parse(model: DumpsysModel, line: String) {

        }
    },
    SLOW_ISSUE_DRAW_COMMANDS("Number Slow issue draw commands") {
        override fun parse(model: DumpsysModel, line: String) {

        }
    },
    NUMBER_FRAME_DEAD_LINE_MISSED("Number Frame deadline missed") {
        override fun parse(model: DumpsysModel, line: String) {

        }
    },
    PROFILE_DATE("PROFILEDATA") {
        override fun parse(model: DumpsysModel, line: String) {
            val profilesDatas = line.split(",")
            if (profilesDatas.isNotEmpty() && profilesDatas[0] == "0") {
                val profileData = ProfileData()
                profileData.flags = profilesDatas[0].toInt()
                profileData.intendedVsync = profilesDatas[1].toLong()
                profileData.vsync = profilesDatas[2].toLong()
                profileData.oldestInputEvent = profilesDatas[3].toLong()
                profileData.newestInputEvent = profilesDatas[4].toLong()
                profileData.handleInputStart = profilesDatas[5].toLong()
                profileData.animationStart = profilesDatas[6].toLong()
                profileData.performTraverSalsStart = profilesDatas[7].toLong()
                profileData.drawStart = profilesDatas[8].toLong()
                profileData.syncQueued = profilesDatas[9].toLong()
                profileData.syncStart = profilesDatas[10].toLong()
                profileData.issueDrawCommandsStart = profilesDatas[11].toLong()
                profileData.swapBuffers = profilesDatas[12].toLong()
                profileData.frameCompleted = profilesDatas[13].toLong()

                model.profileData.add(profileData)
            }
        }
    },
    TOTAL_VIEW_ROOT_IMPL("Total ViewRootImpl") {
        override fun parse(model: DumpsysModel, line: String) {

        }
    },
    TOTAL_VIEWS("Total Views") {
        override fun parse(model: DumpsysModel, line: String) {

        }
    },
    TOTAL_DISPLAY_LIST("Total DisplayList") {
        override fun parse(model: DumpsysModel, line: String) {

        }
    };

    companion object {

        fun parse(model: DumpsysModel, builder: StringBuilder) {
            val lines = builder.split("\n")
            lines.forEachIndexed { i, s ->
                when {
                    s.split(":").size == 2 -> {
                        val splittedLine = s.split(":")
                        get(splittedLine[0])?.parse(model, splittedLine[1])
                    }
                    s.split(",").size >= 16 -> {
                        PROFILE_DATE.parse(model, s)
                    }
                }
            }
        }

        fun parse(model: DumpsysModel, line: String) {
            when {
                line.split(":").size == 2 -> {
                    val splittedLine = line.split(":")
                    get(splittedLine[0])?.parse(model, splittedLine[1])
                }
                line.split(",").size >= 16 -> {
                    PROFILE_DATE.parse(model, line)
                }
            }
        }

        fun get(key: String): DumpsysParserEnums? = values().firstOrNull {
            it.key == key
        }

    }

}

interface DumpsysEnumsActions {
    fun parse(model: DumpsysModel, line: String)
}