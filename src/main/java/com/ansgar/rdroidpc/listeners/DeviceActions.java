package com.ansgar.rdroidpc.listeners;

import java.io.IOException;
import java.io.InputStream;

public interface DeviceActions {

    InputStream getInputStream(String command) throws IOException;

    void disableAccelerometer(int disable);

    void changeOrientation(int orientation);

    void screenRecord(int width, int height, int time, int bitRate);

    void screenCapture(int width, int height);

    void destroy();

}
