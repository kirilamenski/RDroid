package com.ansgar.rdroidpc.ui.components;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class NavigationBottomPanel extends JPanel {

    private int iconWidth = -1, iconHeight = -1;
    private String[] icons;
    private OnNavigationPanelListener listener;

    public void setIconSize(int iconWidth, int iconHeight) {
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
    }

    public void setIcons(String... icons) {
        this.icons = icons;
    }

    public void setItemClickListener(OnNavigationPanelListener listener) {
        this.listener = listener;
    }

    public void createPanel() {
        ClassLoader classLoader = getClass().getClassLoader();
        setLayout(null);
        if (icons != null) {
            int itemWidth = getWidth() / icons.length;
            for (int i = 0; i < icons.length; i++) {
                add(createItem(i, itemWidth, getHeight(), classLoader.getResource(icons[i])));
            }
        }
    }

    private JButton createItem(int position, int width, int height, URL iconPath) {
        JButton button = new JButton();
        button.setBounds(width * position, 0, width, height);
        ImageIcon icon = new ImageIcon(getResizedIcon(iconPath));
        button.setIcon(icon);
        button.setBackground(getBackground());
        button.setHorizontalAlignment(JLabel.CENTER);
        button.setVerticalAlignment(JLabel.CENTER);
        button.addActionListener(e -> {
            if (listener != null) listener.onItemClick(position);
        });
        return button;
    }

    private Image getResizedIcon(URL iconPath) {
        if (iconWidth == -1 || iconHeight == -1) {
            iconWidth = iconHeight = (int) (getHeight() * 0.3);
        }
        return new ImageIcon(iconPath).getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_DEFAULT);
    }

    public interface OnNavigationPanelListener {
        void onItemClick(int id);
    }

}
