package com.ansgar.rdroidpc.utils;

import java.util.HashMap;

public class SharedValues<T> {

    private static SharedValues instance;
    private static HashMap<String, Object> sharedVal;

    public static void newInstance() {
        if (instance == null) {
            synchronized (SharedValues.class) {
                if (instance == null) {
                    instance = new SharedValues();
                }
            }
        }
    }

    private SharedValues() {
        sharedVal = new HashMap<>();
    }

    public static void put(String key, int value) {
        sharedVal.put(key, value);
    }

    public static void put(String key, double value) {
        sharedVal.put(key, value);
    }

    public static void put(String key, float value) {
        sharedVal.put(key, value);
    }

    public static void put(String key, String value) {
        sharedVal.put(key, value);
    }

    public static void put(String key, boolean value) {
        sharedVal.put(key, value);
    }

    public static void put(String key, long value) {
        sharedVal.put(key, value);
    }

    public static <T> void put(String key, T value) {
        sharedVal.put(key, value);
    }

    public static int get(String key, int defaultValue) {
        return (int) getValue(key, defaultValue);
    }

    public static double get(String key, double defaultValue) {
        return (double) getValue(key, defaultValue);
    }

    public static float get(String key, float defaultValue) {
        return (float) getValue(key, defaultValue);
    }

    public static String get(String key, String defaultValue) {
        return (String) getValue(key, defaultValue);
    }

    public static boolean get(String key, boolean defaultValue) {
        return (boolean) getValue(key, defaultValue);
    }

    public static long get(String key, long defaultValue) {
        return (long) getValue(key, defaultValue);
    }

    public static <T> Object get(String key, T defaultValue) {
        return getValue(key, defaultValue);
    }

    private static Object getValue(String key, Object defaultValue) {
        if (sharedVal != null && sharedVal.containsKey(key)) {
            return sharedVal.get(key);
        } else {
            return defaultValue;
        }
    }

}
