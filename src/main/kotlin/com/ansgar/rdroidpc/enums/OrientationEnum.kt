package com.ansgar.rdroidpc.enums

enum class OrientationEnum {
    PORTRAIT,
    LANDSCAPE;

    companion object {
        fun getFromValue(value: String): OrientationEnum {
            val c = value[0]
            return if (value.isNotEmpty() && Character.isDigit(c)) {
                if (c == '0') {
                    PORTRAIT
                } else {
                    LANDSCAPE
                }
            } else {
                PORTRAIT
            }
        }
    }
}