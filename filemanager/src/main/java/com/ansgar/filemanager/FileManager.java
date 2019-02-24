package com.ansgar.filemanager;

public interface FileManager {
    void save(String fileName, String key, String value);

    void save(String fileName, String key, Object obj);

    String get(String fileName, String key, String defaultValue);

    Object get(String fileName, String key);
}
