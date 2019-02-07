package com.ansgar.rdroidpc;

import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.ProgrammInf;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.ui.frames.MainPanel;
import com.ansgar.rdroidpc.utils.AppUiCustomizationUtil;
import com.ansgar.rdroidpc.utils.SharedValues;
import com.ansgar.rdroidpc.utils.ToolkitUtils;

import java.awt.*;

public class Main {

    public static void main(String[] args) {

        SharedValues.newInstance();
        initScreenSize();
        AppUiCustomizationUtil.customizeApp();

        Rectangle rectangle = DimensionConst.Companion.getMainFrameRect();
        rectangle.x = SharedValues.get(StringConst.SHARED_VAL_SCREEN_WIDTH, 400) / 2
                - DimensionConst.MAIN_WINDOW_WIDTH / 2;

        MainPanel panel = new MainPanel(rectangle, ProgrammInf.NAME + "/" + ProgrammInf.VERSION);
        panel.updateUI();
    }

    private static void initScreenSize() {
        Dimension dimension = ToolkitUtils.getWindowSize();
        SharedValues.put(StringConst.SHARED_VAL_SCREEN_WIDTH, dimension.width);
        SharedValues.put(StringConst.SHARED_VAL_SCREEN_HEIGHT, dimension.height);
    }

}
