package com.ansgar.filemanager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FileManagerImpl implements FileManager {

    private final String SEPARATOR = ":";
    private String defaultDirectory;

    public FileManagerImpl() {
        Path path = Paths.get("cache");
        createDirectory(path);
        defaultDirectory = path.toAbsolutePath().toString();
    }

    @Override
    public void save(String fileName, String key, String value) {
        write(fileName, formatValue(key, value + "\n").getBytes(), StandardOpenOption.CREATE);
    }

    @Override
    public <T> void save(String fileName, T obj) {
        Gson gson = new Gson();
        String json = gson.toJson(obj) + "\n";
        write(fileName, json.getBytes(), StandardOpenOption.CREATE);
    }

    @Override
    public <T> void save(String fileName, String key, T obj) {
        if (getClass(fileName, key, obj.getClass()) != null) {
            // TODO
            System.out.println("Exists: " + key);
            return;
        }
        Gson gson = new Gson();
        HashMap<String, T> map = new HashMap<>();
        map.put(key, obj);
        String json = gson.toJson(map) + "\n";
        write(fileName, json.getBytes(), StandardOpenOption.APPEND);
    }

    @Override
    public String get(String fileName, String key, String defaultValue) {
        return read(fileName, key, defaultValue);
    }

    @Override
    public <T> T get(String fileName, Class<T> clazz) {
        List<String> list = getAllLines(fileName);
        String json = (list != null && list.size() > 0) ? list.get(0) : "";
        T obj = new Gson().fromJson(json, clazz);
        return obj;
    }

    @Override
    public <T> T getClass(String fileName, String key, Class<T> clazz) {
        List<String> list = getAllLines(fileName);
        Gson gson = new Gson();

        if (list != null) {
            for (String line : list) {
                Type type = new TypeToken<HashMap<String, T>>() {
                }.getType();
                HashMap<String, T> map = gson.fromJson(line, type);
                if (map.get(key) != null) {
                    return map.get(key);
                }
            }
        }
        return null;
    }

    private void write(String fileName, byte[] bytes, StandardOpenOption option) {
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

    private List<String> getAllLines(String fileName) {
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

    private String formatValue(String key, String value) {
        return String.format("%s%s %s\n", key, SEPARATOR, value);
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
