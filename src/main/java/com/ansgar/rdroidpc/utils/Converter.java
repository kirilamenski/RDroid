package com.ansgar.rdroidpc.utils;

public class Converter {

    public static float convertCoordinates(float originalHeight, float currentHeight, float coordinate) {
        return (originalHeight / currentHeight) * coordinate;
    }

}
