package com.ansgar.filemanager;

import com.google.gson.Gson;
import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FileManagerImpl implements FileManager {

    private final String DIRECTORY = Paths.get("cache").toAbsolutePath().toString();
    private final String SEPARATOR = ":";

    @Override
    public void save(String fileName, String key, String value) {
        write(fileName, formatValue(key, value).getBytes(), StandardOpenOption.CREATE);
    }

    @Override
    public <T> void save(String fileName, T obj) {
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        write(fileName, json.getBytes(), StandardOpenOption.CREATE);
    }

    @Override
    public String get(String fileName, String key, String defaultValue) {
        return read(fileName, key, defaultValue);
    }

    @Override
    @Nullable
    public <T> T get(String fileName, Class<T> clazz) {
        List<String> list = getAllLines(fileName);
        String json = (list != null && list.size() > 0) ? list.get(0) : "";
        T obj = new Gson().fromJson(json, clazz);
        return obj;
    }

    private void write(String fileName, byte[] bytes, StandardOpenOption option) {
        Path path = Paths.get(DIRECTORY, fileName);
        try {
            Files.write(path, bytes, option);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String read(String fileName, String key, String defaultValue) {
        AtomicReference<String> atomicValue = new AtomicReference<>(defaultValue);
        List<String> lines = getAllLines(fileName);
        if (lines != null) {
            lines.forEach(line -> {
                String[] splitted = line.split(SEPARATOR);
                if (splitted.length > 1) {
                    String keyOfValue = splitted[0].trim();
                    if (keyOfValue.equals(key)) {
                        atomicValue.set(splitted[1]);
                    }
                }
            });
        }
        return atomicValue.get();
    }

    @Nullable
    private List<String> getAllLines(String fileName) {
        Path path = Paths.get(DIRECTORY, fileName);
        if (Files.exists(path)) {
            try {
                return Files.readAllLines(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String formatValue(String key, String value) {
        return String.format("%s%s %s\n", key, SEPARATOR, value);
    }

}
