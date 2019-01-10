package com.ansgar.rdroidpc.listeners.impl;

import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.MenuItemsEnum;
import com.ansgar.rdroidpc.constants.SharedValuesKey;
import com.ansgar.rdroidpc.listeners.OnMenuItemListener;
import com.ansgar.rdroidpc.ui.MainPanel;
import com.ansgar.rdroidpc.ui.SettingsFrame;
import com.ansgar.rdroidpc.utils.SharedValues;

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
                    SettingsFrame settingsFrame = new SettingsFrame(rectangle, MenuItemsEnum.SETTINGS.getValue());
                    settingsFrame.setListener(this);
                    break;
                case EXIT:
                    panel.closeDevicesConnections();
                    break;
            }
        }
    }

    @Override
    public void onAdbPathChanged(String path) {
        if (!SharedValues.get(SharedValuesKey.ADB_PATH, "").equals(path)) {
            SharedValues.put(SharedValuesKey.ADB_PATH, path);
            panel.closeDevicesConnections();
        }
    }

}
