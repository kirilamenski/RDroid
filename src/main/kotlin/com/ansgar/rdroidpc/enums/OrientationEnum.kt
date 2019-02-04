package com.ansgar.rdroidpc.enums

enum class OrientationEnum(val value: Int) {
    PORTRAIT(0),
    LANDSCAPE(1);

    companion object {
        fun getFromValue(value: String): OrientationEnum? = OrientationEnum.values()
                .lastOrNull { it.value == value.toInt() }
    }
}