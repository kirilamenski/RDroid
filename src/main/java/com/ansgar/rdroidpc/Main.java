package com.ansgar.rdroidpc;

import com.ansgar.filemanager.FileManagerImpl;
import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.ProgrammInf;
import com.ansgar.rdroidpc.constants.SharedValuesKey;
import com.ansgar.rdroidpc.entities.FilesEnum;
import com.ansgar.rdroidpc.ui.frames.MainPanel;
import com.ansgar.rdroidpc.utils.AppUiCustomizationUtil;
import com.ansgar.rdroidpc.utils.SharedValues;
import com.ansgar.rdroidpc.utils.ToolkitUtils;
import com.ansgar.rdroidpc.utils.TotalMemoryUtils;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SharedValues.newInstance();
        initAdbPath();
        initScreenSize();
        AppUiCustomizationUtil.customizeApp();

        TotalMemoryUtils totalMemoryUtils = new TotalMemoryUtils();
        totalMemoryUtils.start(10000, 20000);

        Rectangle rectangle = new Rectangle(10, 10,
                DimensionConst.MAIN_WINDOW_WIDTH, DimensionConst.MAIN_WINDOW_HEIGHT);
        new MainPanel(rectangle, ProgrammInf.NAME + ProgrammInf.VERSION);
    }

    private static void initScreenSize() {
        Dimension dimension = ToolkitUtils.getWindowSize();
        SharedValues.put(SharedValuesKey.SHARED_VAL_SCREEN_WIDTH, dimension.width);
        SharedValues.put(SharedValuesKey.SHARED_VAL_SCREEN_HEIGHT, dimension.height);
    }

    private static void initAdbPath() {
        String adbPath = new FileManagerImpl(FilesEnum.CACHE.getValue())
                .get(FilesEnum.SETTINGS.getValue(), SharedValuesKey.ADB_PATH);
        SharedValues.put(SharedValuesKey.ADB_PATH, adbPath != null ? adbPath : "");
    }

}
