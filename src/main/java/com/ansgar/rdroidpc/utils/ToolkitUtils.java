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
        try {
            //TODO Can't paste characters like ' " and etc. Need to find the way to replace them with unicode.
            return ((String) Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .getData(DataFlavor.stringFlavor))
                    .replace(" ", "\u0020")
                    .replace("`", "\u0060")
                    .replace("'", "\u0060");
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
