package com.ansgar.rdroidpc.ui.components.menu;

import com.ansgar.rdoidpc.constants.Colors;
import com.ansgar.rdoidpc.constants.DimensionConst;
import com.ansgar.rdoidpc.constants.StringConst;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MenuBar extends JMenuBar {

    public MenuBar() {

    }

    public MenuBar getMenuBar() {
        setBackground(Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBounds(0, 0, DimensionConst.MAIN_WINDOW_WIDTH, 25);
        setBorder(new MatteBorder(0, 0, 0, 0, Color.BLACK));
        displayMenus();

        return this;
    }

    private void displayMenus() {
        for (Menu menu : getMenuList()) {
            add(menu);
        }

        updateUI();
    }

    private List<Menu> getMenuList() {
        List<Menu> menus = new ArrayList<>();

        Menu menuFile = new Menu(StringConst.MENU_TITLE_FILE);
        JMenuItem menuItem = new JMenuItem("Exit");
        menuItem.setBackground(Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        menuItem.addActionListener(menuActionListener);
        menuFile.add(menuItem);
        Menu menuHelp = new Menu(StringConst.MENU_TITLE_HELP);
        Menu menuSettings = new Menu(StringConst.MENU_SETTINGS);

        menus.add(menuFile.getMenu());
        menus.add(menuSettings.getMenu());
        menus.add(menuHelp.getMenu());
        return menus;
    }

    private ActionListener menuActionListener = e -> {
        switch (e.getActionCommand()) {
            case "Exit":
                System.exit(0);
                break;
        }
    };
}
