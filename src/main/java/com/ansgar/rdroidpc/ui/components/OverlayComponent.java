package com.ansgar.rdroidpc.ui.components;

import com.ansgar.rdroidpc.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class OverlayComponent extends JPanel {

    private String imageSrc;
    private int imageWidth, imageHeight;
    private JLabel titleL;

    public OverlayComponent() {
        setLayout(null);
    }

    public void createComponent() {
        setBackground(new Color(0, 0, 0, 0));
        if (imageSrc != null && !imageSrc.isEmpty()) {
            JLabel iconL = new JLabel(new ImageIcon(getImage(imageSrc)));
            iconL.setBounds(0, 0, imageWidth, imageHeight);
            add(iconL);
        }

        titleL = new JLabel("Wait");
        titleL.setBounds(imageWidth, getHeight() / 6, getWidth() - imageWidth, getHeight());
        add(titleL);
    }

    private Image getImage(String src) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL imageUrl = classLoader.getResource(src);
        return ImageUtils.resizeIcon(imageUrl, imageWidth, imageHeight);
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public void updateTitle(String title) {
        if (titleL != null) titleL.setText(title);
    }
}
