package com.ansgar.rdroidpc.utils;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ImageUtils {

    @Nullable
    public static Image resizeIcon(URL iconSource, int width, int height) {
        if (iconSource == null) return null;
        return new ImageIcon(iconSource)
                .getImage()
                .getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

}
