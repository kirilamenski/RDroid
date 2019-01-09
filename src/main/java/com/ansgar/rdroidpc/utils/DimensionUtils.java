package com.ansgar.rdroidpc.utils;

import com.ansgar.rdroidpc.constants.OsEnum;

public class DimensionUtils {

    public static float convertCoordinates(float originalHeight, float currentHeight, float coordinate) {
        return (originalHeight / currentHeight) * coordinate;
    }

}
