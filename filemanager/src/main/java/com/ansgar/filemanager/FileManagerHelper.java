package com.ansgar.filemanager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

class FileManagerHelper {

    private final String SEPARATOR = ":";
    private String defaultDirectory;

    FileManagerHelper() {
        Path path = Paths.get("cache");
        createDirectory(path);
        defaultDirectory = path.toAbsolutePath().toString();
    }

    void write(String fileName, byte[] bytes, StandardOpenOption option) {
        Path path = Paths.get(defaultDirectory, fileName);
        if (!Files.exists(path)) {
            option = StandardOpenOption.CREATE;
        }
        try {
            Files.write(path, bytes, option);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String read(String fileName, String key, String defaultValue) {
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

    List<String> getAllLines(String fileName) {
        Path path = Paths.get(defaultDirectory, fileName);
        if (Files.exists(path)) {
            try {
                return Files.readAllLines(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    String formatValue(String key, String value) {
        return String.format("%s%s %s\n", key, SEPARATOR, value);
    }

    @NotNull
    <T> HashMap<String, T> getHashedLines(String fileName) {
        List<String> list = getAllLines(fileName);
        HashMap<String, T> map = new HashMap<>();
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, T>>() {
        }.getType();

        if (list != null) {
            list.forEach(line -> map.putAll(gson.fromJson(line, type)));
        }
        return map;
    }

    private void createDirectory(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
