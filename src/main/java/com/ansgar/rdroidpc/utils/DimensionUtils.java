package com.ansgar.rdroidpc.utils;

public class DimensionUtils {

    public static float convertCoordinates(float originalHeight, float currentHeight, float coordinate) {
        return (originalHeight / currentHeight) * coordinate;
    }

}
