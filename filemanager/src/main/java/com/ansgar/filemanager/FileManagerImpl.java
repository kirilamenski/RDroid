package com.ansgar.filemanager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;

public class FileManagerImpl implements FileManager {

    private FileManagerHelper fileManagerHelper;

    public FileManagerImpl() {
        fileManagerHelper = new FileManagerHelper();
    }

    @Override
    public void save(String fileName, String key, String value) {
        fileManagerHelper.write(fileName, fileManagerHelper.formatValue(key, value + "\n").getBytes(),
                StandardOpenOption.CREATE);
    }

    @Override
    public <T> void save(String fileName, T obj) {
        Gson gson = new Gson();
        String json = gson.toJson(obj) + "\n";
        fileManagerHelper.write(fileName, json.getBytes(), StandardOpenOption.CREATE);
    }

    @Override
    public <T> void save(String fileName, String key, T obj) {
        Gson gson = new Gson();
        HashMap<String, T> map = fileManagerHelper.getHashedLines(fileName);
        map.put(key, obj);
        String json = gson.toJson(map);
        fileManagerHelper.write(fileName, json.getBytes(), StandardOpenOption.CREATE);
    }

    @Override
    public String get(String fileName, String key, String defaultValue) {
        return fileManagerHelper.read(fileName, key, defaultValue);
    }

    @Override
    public <T> T get(String fileName, Class<T> clazz) {
        List<String> list = fileManagerHelper.getAllLines(fileName);
        String json = (list != null && list.size() > 0) ? list.get(0) : "";
        T obj = new Gson().fromJson(json, clazz);
        return obj;
    }

    @Override
    public <T> T getClass(String fileName, String key, Class<T> clazz) {
        HashMap<String, T> map = fileManagerHelper.getHashedLines(fileName);
        Gson gson = new Gson();
        if (map != null) {
            JsonObject jsonObj = gson.toJsonTree(map.get(key))
                    .getAsJsonObject();
            return gson.fromJson(jsonObj.toString(), clazz);
        }
        return null;
    }

}
