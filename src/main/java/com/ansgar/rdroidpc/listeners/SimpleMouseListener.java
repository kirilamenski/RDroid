package com.ansgar.rdroidpc.listeners;

import com.ansgar.rdroidpc.entities.ComponentProperties;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SimpleMouseListener implements MouseListener {

    @NotNull
    private Component component;
    @NotNull
    private ComponentProperties properties;

    public SimpleMouseListener(@NotNull Component component) {
        this.component = component;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        component.setBackground(properties.getPressedColor());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        component.setBackground(properties.getColor());
    }

    public void setProperties(@NotNull ComponentProperties properties) {
        this.properties = properties;
    }
}
