package com.ansgar.rdroidpc.listeners.impl;

import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.enums.MenuItemsEnum;
import com.ansgar.rdroidpc.constants.SharedValuesKey;
import com.ansgar.rdroidpc.listeners.OnMenuItemListener;
import com.ansgar.rdroidpc.ui.frames.MainPanel;
import com.ansgar.rdroidpc.ui.frames.SettingsFrame;
import com.ansgar.rdroidpc.utils.SharedValues;

import java.awt.*;

public class MainPanelMenuListenerImpl implements OnMenuItemListener {

    private MainPanel panel;

    public MainPanelMenuListenerImpl(MainPanel panel) {
        this.panel = panel;
    }

    @Override
    public void onItemClicked(String name) {
        MenuItemsEnum menuItemsEnum = MenuItemsEnum.Companion.getMenuItemEnumByValue(name);
        if (menuItemsEnum != null) {
            switch (menuItemsEnum) {
                case SETTINGS:
                    Rectangle rectangle = DimensionConst.Companion.getSettingFrameRect();
                    rectangle.x = panel.getRectangle().x;
                    rectangle.y = panel.getRectangle().y;
                    SettingsFrame settingsFrame = new SettingsFrame(rectangle,
                            MenuItemsEnum.SETTINGS.getValue());
                    settingsFrame.setListener(this);
                    break;
                case EXIT:
                    panel.onCloseApp();
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
