package com.ansgar.filemanager;

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
    public void save(String fileName, String key, Object obj) {

    }

    @Override
    public String get(String fileName, String key, String defaultValue) {
        return read(fileName, key, defaultValue);
    }

    @Override
    public Object get(String fileName, String key) {
        return null;
    }

    private void write(String fileName, byte[] bytes, StandardOpenOption option) {
        Path path = Paths.get(DIRECTORY, fileName);
        try {
            System.out.println(DIRECTORY);
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
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String formatValue(String key, String value) {
        return String.format("%s%s %s\n", key, SEPARATOR, value);
    }

}
