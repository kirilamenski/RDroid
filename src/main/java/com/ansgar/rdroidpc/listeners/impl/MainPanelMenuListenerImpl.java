package com.ansgar.rdroidpc.listeners.impl;

import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.MenuItemsEnum;
import com.ansgar.rdroidpc.listeners.OnMenuItemListener;
import com.ansgar.rdroidpc.ui.MainPanel;
import com.ansgar.rdroidpc.ui.SettingsFrame;

import java.awt.*;

public class MainPanelMenuListenerImpl implements OnMenuItemListener {

    private MainPanel panel;

    public MainPanelMenuListenerImpl(MainPanel panel) {
        this.panel = panel;
    }

    @Override
    public void onItemClicked(String name) {
        System.out.println(name);
        MenuItemsEnum menuItemsEnum = MenuItemsEnum.Companion.getMenuItemEnumByValue(name);
        if (menuItemsEnum != null) {
            switch (menuItemsEnum) {
                case SETTINGS:
                    Rectangle rectangle = new Rectangle(700, 100,
                            DimensionConst.SETTINGS_PANEL_WIDTH, DimensionConst.SETTINGS_PANEL_HEIGHT);
                    new SettingsFrame(rectangle, MenuItemsEnum.SETTINGS.getValue());
                    break;
                case EXIT:
                    // TODO in exit block stop all opened connections and restart adb server
                    panel.getAdbBackend().shutdown();
                    System.exit(0);
                    break;
            }
        }
    }

}
