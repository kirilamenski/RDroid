package com.ansgar.rdroidpc.utils;

import java.awt.*;

public class ToolkitUtils {

    public static Dimension getWindowSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public static int getScreenResolution() {
        return Toolkit.getDefaultToolkit().getScreenResolution();
    }

    public static GraphicsDevice getMultiWindowSize() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }
}
