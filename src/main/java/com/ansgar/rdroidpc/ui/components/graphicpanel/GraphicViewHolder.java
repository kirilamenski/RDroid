package com.ansgar.rdroidpc.ui.components.graphicpanel;

import java.awt.*;

public abstract class GraphicViewHolder<T> {

    protected T model;
    protected GraphicPanel panel;

    public void setModel(T model) {
        this.model = model;
    }

    protected abstract void draw(Graphics2D g2d, int position);

}
