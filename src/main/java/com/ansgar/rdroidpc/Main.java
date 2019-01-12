package com.ansgar.rdroidpc;

import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.ProgrammInf;
import com.ansgar.rdroidpc.ui.MainPanel;
import com.ansgar.rdroidpc.utils.AppUiCustomizationUtil;
import com.ansgar.rdroidpc.utils.SharedValues;
import com.ansgar.rdroidpc.utils.ToolkitUtils;

import java.awt.*;

public class Main {

    public static void main(String[] args) {

        SharedValues.newInstance();
        AppUiCustomizationUtil.customizeApp();

        int x = ToolkitUtils.getWindowSize().width / 2 - DimensionConst.MAIN_WINDOW_WIDTH / 2;
        Rectangle rectangle = new Rectangle(x, 100,
                DimensionConst.MAIN_WINDOW_WIDTH, DimensionConst.MAIN_WINDOW_HEIGHT);

        MainPanel panel = new MainPanel(rectangle, ProgrammInf.NAME + "/" + ProgrammInf.VERSION);
        panel.updateUI();
    }

}
