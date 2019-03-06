package com.ansgar.rdroidpc;

import com.ansgar.filemanager.FileManagerImpl;
import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.ProgrammInf;
import com.ansgar.rdroidpc.constants.SharedValuesKey;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.entities.FilesEnum;
import com.ansgar.rdroidpc.ui.frames.MainPanel;
import com.ansgar.rdroidpc.utils.AppUiCustomizationUtil;
import com.ansgar.rdroidpc.utils.SharedValues;
import com.ansgar.rdroidpc.utils.ToolkitUtils;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SharedValues.newInstance();
        initAdbPath();
        initScreenSize();
        AppUiCustomizationUtil.customizeApp();

        Rectangle rectangle = new Rectangle(
                SharedValues.get(StringConst.SHARED_VAL_SCREEN_WIDTH, 400) / 2 - DimensionConst.MAIN_WINDOW_WIDTH / 2,
                100,
                DimensionConst.MAIN_WINDOW_WIDTH,
                DimensionConst.MAIN_WINDOW_HEIGHT
        );

        MainPanel panel = new MainPanel(rectangle, ProgrammInf.NAME + "/" + ProgrammInf.VERSION);
        panel.updateUI();
    }

    private static void initScreenSize() {
        Dimension dimension = ToolkitUtils.getWindowSize();
        SharedValues.put(StringConst.SHARED_VAL_SCREEN_WIDTH, dimension.width);
        SharedValues.put(StringConst.SHARED_VAL_SCREEN_HEIGHT, dimension.height);
    }

    private static void initAdbPath() {
        String adbPath = new FileManagerImpl(FilesEnum.CACHE.getValue())
                .get(FilesEnum.SETTINGS.getValue(), SharedValuesKey.ADB_PATH, "");
        SharedValues.put(SharedValuesKey.ADB_PATH, adbPath != null ? adbPath : "");
    }

}
