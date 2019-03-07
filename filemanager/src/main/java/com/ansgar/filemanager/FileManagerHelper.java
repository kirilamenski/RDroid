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

class FileManagerHelper {

    private String defaultDirectory;

    FileManagerHelper(String folder) {
        Path path = Paths.get(folder);
        createDirectory(path);
        defaultDirectory = path.toAbsolutePath().toString();
    }

    void write(String fileName, byte[] bytes, StandardOpenOption option) {
        Path path = Paths.get(defaultDirectory, fileName);
        createFile(fileName);
        try {
            Files.write(path, bytes, option);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<String> getAllLines(String fileName) {
        Path path = Paths.get(defaultDirectory, fileName);
        createFile(fileName);
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    <T> HashMap<String, T> getHashedLines(String fileName) {
        createFile(fileName);

        HashMap<String, T> map = new HashMap<>();
        List<String> list = getAllLines(fileName);
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

    private void createFile(String fileName) {
        Path path = Paths.get(defaultDirectory, fileName);
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
