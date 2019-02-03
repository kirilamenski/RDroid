package com.ansgar.rdroidpc;

import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.ProgrammInf;
import com.ansgar.rdroidpc.ui.frames.MainPanel;
import com.ansgar.rdroidpc.utils.AppUiCustomizationUtil;
import com.ansgar.rdroidpc.utils.SharedValues;
import com.ansgar.rdroidpc.utils.ToolkitUtils;

import java.awt.*;

public class Main {

    public static void main(String[] args) {

        SharedValues.newInstance();
        AppUiCustomizationUtil.customizeApp();

        int x = ToolkitUtils.getWindowSize().width / 2 - DimensionConst.MAIN_WINDOW_WIDTH / 2;
        Rectangle rectangle = DimensionConst.Companion.getMainFrameRect();
        rectangle.x = x;

        MainPanel panel = new MainPanel(rectangle, ProgrammInf.NAME + "/" + ProgrammInf.VERSION);
        panel.updateUI();
    }

}
