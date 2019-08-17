package com.ansgar.rdroidpc.ui.components;

import com.ansgar.rdroidpc.enums.ButtonsPanelStateEnum;
import com.ansgar.rdroidpc.utils.ImageUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.net.URL;

public class ButtonsPanel extends JPanel {

    private int iconWidth = -1, iconHeight = -1;
    private String[] icons;
    private String[] toolTips;
    private OnButtonPanelListener listener;
    private MatteBorder border;
    private ButtonsPanelStateEnum state;

    public ButtonsPanel() {
        this.border = new MatteBorder(0, 1, 1, 0, Color.BLACK);
        this.state = ButtonsPanelStateEnum.HORIZONTAL;
    }

    public void createPanel() {
        ClassLoader classLoader = getClass().getClassLoader();
        setLayout(null);
        if (icons != null) {
            for (int i = 0; i < icons.length; i++) {
                int width = getWidth();
                int height = getHeight();
                int x = 0;
                int y = 0;
                if (state == ButtonsPanelStateEnum.HORIZONTAL) {
                    width = getWidth() / icons.length;
                    x = width * i;
                } else {
                    height = getHeight() / icons.length;
                    y = height * i;
                }
                add(createItem(i, x, y, width, height, classLoader.getResource(icons[i])));
            }
        }
    }

    private JButton createItem(int position, int x, int y, int width, int height, URL iconPath) {
        JButton button = new JButton();
        button.setBounds(x, y, width, height);
        ImageIcon icon = new ImageIcon(getResizedIcon(iconPath));
        button.setIcon(icon);
        button.setToolTipText(getTooltip(position));
        button.setFocusable(false);
        button.setHorizontalAlignment(JLabel.CENTER);
        button.setVerticalAlignment(JLabel.CENTER);
        button.setBorder(border);
        button.addActionListener(e -> {
            if (listener != null) listener.onActionItemClicked(position);
        });
        return button;
    }

    private Image getResizedIcon(URL iconPath) {
        if (iconWidth == -1 || iconHeight == -1) {
            iconWidth = iconHeight = (int) ((state == ButtonsPanelStateEnum.HORIZONTAL ? getHeight() : getWidth()) * 0.3);
        }
        return ImageUtils.resizeIcon(iconPath, iconWidth, iconHeight);
    }

    @Nullable
    private String getTooltip(int position) {
        if (toolTips != null && toolTips.length > 0
                && icons != null && icons.length > 0
                && position < icons.length) {
            return toolTips[position];
        }
        return null;
    }

    public interface OnButtonPanelListener {
        void onActionItemClicked(int position);
    }

    public void setBorder(MatteBorder border) {
        this.border = border;
    }

    public void setState(ButtonsPanelStateEnum state) {
        this.state = state;
    }

    public void setIconSize(int iconWidth, int iconHeight) {
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
    }

    public void setIcons(String... icons) {
        this.icons = icons;
    }

    public void setToolTips(String... toolTips) {
        this.toolTips = toolTips;
    }

    public void setItemClickListener(OnButtonPanelListener listener) {
        this.listener = listener;
    }

    public static class ButtonsPanelBuilder {

        private ButtonsPanel panel;

        public ButtonsPanelBuilder() {
            panel = new ButtonsPanel();
        }

        public ButtonsPanelBuilder setBorder(MatteBorder border) {
            panel.setBorder(border);
            return this;
        }

        public ButtonsPanelBuilder setState(ButtonsPanelStateEnum state) {
            panel.setState(state);
            return this;
        }

        public ButtonsPanelBuilder setIconSize(int iconWidth, int iconHeight) {
            panel.setIconSize(iconWidth, iconHeight);
            return this;
        }

        public ButtonsPanelBuilder setIcons(String... icons) {
            panel.setIcons(icons);
            return this;
        }

        public ButtonsPanelBuilder setToolTips(String... toolTips) {
            panel.setToolTips(toolTips);
            return this;
        }

        public ButtonsPanelBuilder setBounds(int x, int y, int width, int height) {
            panel.setBounds(x, y, width, height);
            return this;
        }

        public ButtonsPanelBuilder setBounds(Rectangle rectangle) {
            panel.setBounds(rectangle);
            return this;
        }

        public ButtonsPanelBuilder setItemClickListener(OnButtonPanelListener listener) {
            panel.setItemClickListener(listener);
            return this;
        }

        public ButtonsPanel build() {
            panel.createPanel();
            return panel;
        }

    }

}
