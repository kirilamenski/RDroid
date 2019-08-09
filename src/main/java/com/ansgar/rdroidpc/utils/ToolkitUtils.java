package com.ansgar.rdroidpc.utils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

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

    public static String getTextFromClipboard() {
        StringBuilder savedText = new StringBuilder();
        try {
            savedText.append(Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return savedText.toString();
    }

}
