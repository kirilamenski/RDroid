package com.ansgar.rdroidpc.utils;

public class DimensionUtils {

    public static float convertCoordinates(float originalSize, float currentSize, float coordinate) {
        return (originalSize / currentSize) * coordinate;
    }

}
