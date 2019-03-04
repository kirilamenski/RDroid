package com.ansgar.filemanager;

public interface FileManager {
    void save(String fileName, String key, String value);

    <T> void save(String fileName, T obj);

    <T> void save(String fileName, String key, T obj);

    String get(String fileName, String key, String defaultValue);

    <T> T get(String fileName, Class<T> clazz);

    <T> T getClass(String fileName, String key, Class<T> clazz);
}
