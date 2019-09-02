package com.ansgar.graphic;

import java.awt.*;

public abstract class GraphicViewHolder<T> {

    protected T model;
    protected GraphicPanel panel;
    private int startX, endX;

    public void setModel(T model) {
        this.model = model;
    }

    protected abstract void draw(Graphics2D g2d, int position);

    public int getWidth() {
        return endX - startX;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }
}
