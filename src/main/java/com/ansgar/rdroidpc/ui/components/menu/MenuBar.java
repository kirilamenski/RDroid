package com.ansgar.rdroidpc.ui.components.menu;

import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.listeners.OnMenuItemListener;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuBar extends JMenuBar {

    private OnMenuItemListener listener;

    public MenuBar() {

    }

    public MenuBar getMenuBar(Serializable[] menus) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBounds(0, 0, DimensionConst.MAIN_WINDOW_WIDTH, 25);
        setBorder(new MatteBorder(0, 0, 0, 0, Color.BLACK));
        displayMenus(menus);

        return this;
    }

    private void displayMenus(Serializable[] menus) {
        for (Menu menu : parseMenuList(menus)) {
            add(menu);
        }
    }

    /**
     * Parsing Serializable[] property. If structure of {@param menuList} is kind of {"", {"",""}, ""}
     * it means that the first string value is {@link Menu} following array is {@link JMenuItem} of previous menu item
     *
     * @param menuList serializable value which contain menu for display
     * @return generated List of {@link Menu}
     */
    private List<Menu> parseMenuList(Serializable[] menuList) {
        List<Menu> menus = new ArrayList<>();
        Menu menu = null;
        for (Serializable item : menuList) {
            if (item instanceof String) {
                menu = createMenu((String) item);
            } else if (item instanceof String[]) {
                if (menu != null) {
                    String[] items = (String[]) item;
                    for (String item1 : items) {
                        menu.add(createMenuItem(item1));
                    }
                }
            }
            if (menu != null) menus.add(menu.getMenu());
        }
        return menus;
    }

    private Menu createMenu(String name) {
        Menu menu = new Menu(name);
        return menu;
    }

    private JMenuItem createMenuItem(String name) {
        JMenuItem menuItem = new JMenuItem(name);
        menuItem.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
        menuItem.addActionListener(menuActionListener);
        return menuItem;
    }

    private ActionListener menuActionListener = e -> {
        if (listener != null) listener.onMenuItemClicked(e.getActionCommand());
    };

    public void setListener(OnMenuItemListener listener) {
        this.listener = listener;
    }
}
