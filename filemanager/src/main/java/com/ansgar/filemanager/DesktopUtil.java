package com.ansgar.filemanager;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class DesktopUtil {

    public static void openFolder(String path) throws IOException {
        Desktop.getDesktop().open(new File(path));
    }

    public static void browse(URI uri) throws IOException {
        Desktop.getDesktop().browse(uri);
    }

}
