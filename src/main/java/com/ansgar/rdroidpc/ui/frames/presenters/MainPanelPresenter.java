package com.ansgar.rdroidpc.ui.frames.presenters;

import com.ansgar.filemanager.FileManagerImpl;
import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.SharedValuesKey;
import com.ansgar.rdroidpc.entities.FilesEnum;
import com.ansgar.rdroidpc.enums.MainMenuItemsEnum;
import com.ansgar.rdroidpc.listeners.OnSettingsListener;
import com.ansgar.rdroidpc.ui.frames.AboutPanel;
import com.ansgar.rdroidpc.ui.frames.SettingsFrame;
import com.ansgar.rdroidpc.ui.frames.views.MainPanelView;
import com.ansgar.rdroidpc.utils.SharedValues;

import java.awt.*;

public class MainPanelPresenter extends BasePresenter<MainPanelView> implements OnSettingsListener {

    public MainPanelPresenter(MainPanelView view) {
        super(view);
    }

    public void openSettings() {
        Rectangle rectangle = new Rectangle(
                view.getRectangle().x,
                view.getRectangle().y,
                DimensionConst.SETTINGS_PANEL_WIDTH,
                DimensionConst.SETTINGS_PANEL_HEIGHT
        );
        SettingsFrame settingsFrame = new SettingsFrame(view.getChildComponent(), rectangle,
                MainMenuItemsEnum.SETTINGS.getValue());
        settingsFrame.setListener(this);
    }

    public void openInformation() {
        new AboutPanel(new Rectangle(view.getRectangle()), MainMenuItemsEnum.INFORMATION.getValue());
    }

    @Override
    public void onAdbPathChanged(String path) {
        new FileManagerImpl(FilesEnum.CACHE.getValue())
                .save(FilesEnum.SETTINGS.getValue(), SharedValuesKey.ADB_PATH, path);
        SharedValues.put(SharedValuesKey.ADB_PATH, path);
        view.closeDevicesConnections();
    }

}
