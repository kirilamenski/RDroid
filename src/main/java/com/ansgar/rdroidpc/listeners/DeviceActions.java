package com.ansgar.rdroidpc.listeners;

import com.ansgar.rdroidpc.entities.ScreenRecordOptions;

import java.io.IOException;
import java.io.InputStream;

public interface DeviceActions {

    InputStream getInputStream(String command) throws IOException;

    void disableAccelerometer(int disable);

    void changeOrientation(int orientation);

    void screenRecord(ScreenRecordOptions options, String name, OnSaveScreenListener listener);

    void screenCapture(String fileName, String path, OnSaveScreenListener listener);

    void restart();

    void destroy();

}
