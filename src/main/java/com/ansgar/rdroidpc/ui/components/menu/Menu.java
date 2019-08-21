package com.ansgar.rdroidpc.ui.components.menu;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class Menu extends JMenu {

    private String name;
    private String color = "#ffffff";

    public Menu(String name) {
        this.name = name;
    }

    public Menu getMenu() {
        setText(name);
        setMnemonic(name.charAt(0));
        setBorder(new MatteBorder(0,0,0,0,Color.BLACK));
        setForeground(Color.decode(color));
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
