package com.ansgar.rdroidpc.utils;

public class DimensionUtils {

    public static float convertCoordinates(float originalSize, float currentSize, float coordinate) {
        return (originalSize / currentSize) * coordinate;
    }

    public static double round(float value, double count) {
        return Math.round(value * count) / count;
    }

}
