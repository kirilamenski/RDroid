package com.ansgar.filemanager;

public interface FileManager {
    void save(String fileName, String key, String value);

    <T> void save(String fileName, T obj);

    String get(String fileName, String key, String defaultValue);

    <T> T get(String fileName, Class<T> clazz);
}
