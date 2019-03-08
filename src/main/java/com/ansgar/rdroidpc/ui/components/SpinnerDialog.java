package com.ansgar.rdroidpc.ui.components;

import com.ansgar.rdroidpc.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.net.URL;
import java.util.List;

/**
 * This class simulate spinner loading dialog which extend from {@link SwingWorker}.
 * To call it you need to override doInBackground method where you execute you operation.
 * In this method you should call publish method. After that dialog will run and dispose automatically.
 */
public abstract class SpinnerDialog extends SwingWorker<Void, Void> {

    private Rectangle rectangle;
    private final JDialog dialog = new JDialog();
    private String spinnerSrc = "gifs/loading.gif";
    private int imageWidth = 60;
    private int imageHeight = 60;
    private boolean undecorated = true;

    public SpinnerDialog(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    protected void process(List<Void> chunks) {
        initDialog();
    }

    @Override
    protected void done() {
        dialog.dispose();
    }

    @Override
    protected Void doInBackground() {
        publish();
        doInBack();
        return null;
    }

    public abstract void doInBack();

    private void initDialog() {
        dialog.getContentPane().add(createSpinner());
        dialog.setUndecorated(undecorated);
        dialog.pack();
        dialog.setLocation(rectangle.x, rectangle.y);
        dialog.setSize(new Dimension(rectangle.width, rectangle.height));
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setModal(true);
        dialog.setVisible(true);
    }

    private JPanel createSpinner() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL imageUrl = classLoader.getResource(spinnerSrc);
        Image img = ImageUtils.resizeIcon(imageUrl, imageWidth, imageHeight);
        if (img != null) {
            JPanel spinnerContainer = new JPanel(null);
            ImageIcon icon = new ImageIcon(img);
            JLabel spinner = new JLabel(icon);
            spinner.setBounds(
                    (int) (rectangle.getWidth() / 2 - icon.getIconWidth() / 2),
                    (int) (rectangle.getHeight() / 2 - icon.getIconHeight() / 2),
                    icon.getIconWidth(), icon.getIconHeight());
            spinnerContainer.add(spinner);
            return spinnerContainer;
        }

        return null;
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

    public void setUndecorated(boolean undecorated) {
        this.undecorated = undecorated;
    }

}
