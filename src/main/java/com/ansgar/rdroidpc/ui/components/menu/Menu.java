package com.ansgar.rdroidpc.ui.menu;

import javax.swing.*;
import java.awt.*;

public class Menu extends JMenu {

    private String name;
    private String color = "#ffffff";

    public Menu() {

    }

    public Menu(String name) {
        this.name = name;
    }

    public Menu getMenu() {
        setText(name);
        setBounds(0, 0, 200, 100);
        setMnemonic(name.charAt(0));
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
