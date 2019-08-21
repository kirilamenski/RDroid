package com.ansgar.rdroidpc.utils;

import java.util.HashMap;

public class SharedValues {

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

    public static <T> void put(String key, T value) {
        sharedVal.put(key, value);
    }

    public static <T> T get(String key, T defaultValue) {
        return getValue(key, defaultValue);
    }

    private static <T> T getValue(String key, T defaultValue) {
        if (sharedVal != null && sharedVal.containsKey(key)) {
            return (T) sharedVal.get(key);
        } else {
            return defaultValue;
        }
    }

}
