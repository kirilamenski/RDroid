package com.ansgar.graphic;

import java.awt.*;

public abstract class GraphicViewHolder<T> {

    protected T model;
    protected GraphicPanel panel;
    private int width, height;

    public void setModel(T model) {
        this.model = model;
    }

    protected abstract void draw(Graphics2D g2d, int position);

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
