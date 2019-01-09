package com.ansgar.rdroidpc.constants

enum class OsEnum(val osType: String, val heightOfset: Int) {

    LINUX("", 48),
    MAC_OS("Mac Os X", 22),
    WINDOWS("", 48);

    companion object {
        fun getOsType(): OsEnum {
            val os = System.getProperty("os.name")
            values().forEach { type ->
                if (type.osType.toLowerCase() == os.toLowerCase()) return type
            }
            return LINUX
        }
    }

}