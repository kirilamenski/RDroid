package com.ansgar.rdroidpc.listeners.impl;

import com.ansgar.filemanager.FileManagerImpl;
import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.entities.FilesEnum;
import com.ansgar.rdroidpc.enums.MenuItemsEnum;
import com.ansgar.rdroidpc.constants.SharedValuesKey;
import com.ansgar.rdroidpc.listeners.OnMenuItemListener;
import com.ansgar.rdroidpc.ui.frames.AboutPanel;
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
    public void onMenuItemClicked(String name) {
        MenuItemsEnum menuItemsEnum = MenuItemsEnum.Companion.getMenuItemEnumByValue(name);
        if (menuItemsEnum != null) {
            switch (menuItemsEnum) {
                case SETTINGS:
                    Rectangle rectangle = new Rectangle(
                            panel.getRectangle().x,
                            panel.getRectangle().y,
                            DimensionConst.SETTINGS_PANEL_WIDTH,
                            DimensionConst.SETTINGS_PANEL_HEIGHT
                    );
                    SettingsFrame settingsFrame = new SettingsFrame(panel, rectangle,
                            MenuItemsEnum.SETTINGS.getValue());
                    settingsFrame.setListener(this);
                    break;
                case INFORMATION:
                    new AboutPanel(panel.getRectangle(), MenuItemsEnum.INFORMATION.getValue());
                    break;
                case EXIT:
                    panel.onCloseFrame();
                    break;
            }
        }
    }

    @Override
    public void onAdbPathChanged(String path) {
        new FileManagerImpl(FilesEnum.CACHE.getValue())
                .save(FilesEnum.SETTINGS.getValue(), SharedValuesKey.ADB_PATH, path);
        SharedValues.put(SharedValuesKey.ADB_PATH, path);
        panel.closeDevicesConnections();
    }

}
