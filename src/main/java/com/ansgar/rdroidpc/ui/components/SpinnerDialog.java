package com.ansgar.rdroidpc.ui.components;

import com.ansgar.rdroidpc.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SpinnerDialog implements Runnable {

    private String spinnerSrc = "gifs/loading.gif";
    private int imageWidth = 60;
    private int imageHeight = 60;
    private Thread thread;
    private JFrame frame;
    private JDialog dialog;

    public SpinnerDialog(JFrame frame) {
        this.frame = frame;
    }

    private JPanel createSpinner() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL imageUrl = classLoader.getResource(spinnerSrc);
        Image img = ImageUtils.resizeIcon(imageUrl, imageWidth, imageHeight);
        if (img != null) {
            JPanel spinnerContainer = new JPanel(new BorderLayout());
            ImageIcon icon = new ImageIcon(img);
            JLabel spinner = new JLabel(icon);
            spinnerContainer.add(spinner);

            return spinnerContainer;
        }

        return null;
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void close() {
        if (thread != null) {
            dialog.dispose();
            try {
                thread.join();
                thread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public SpinnerDialog setSpinnerSrc(String spinnerSrc) {
        this.spinnerSrc = spinnerSrc;
        return this;
    }

    public SpinnerDialog setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
        return this;
    }

    public SpinnerDialog setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
        return this;
    }

    @Override
    public void run() {
        dialog = new JDialog(frame);
        dialog.getContentPane().add(createSpinner());
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        dialog.pack();
        dialog.setSize(new Dimension(dialog.getParent().getWidth(), dialog.getParent().getHeight()));
        dialog.setLocationRelativeTo(dialog.getParent());
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setModal(true);
        dialog.setVisible(true);
    }
}
