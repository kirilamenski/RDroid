package com.ansgar.rdroidpc.utils;

import com.ansgar.rdroidpc.constants.Colors;

import javax.swing.*;
import java.awt.*;

public class AppUiCustomizationUtil {

    public static void customizeApp() {
        UIManager.put("ScrollPane.background", Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        UIManager.put("List.background", Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        UIManager.put("List.foreground", Color.WHITE);

        UIManager.put("Panel.background", Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        UIManager.put("Panel.foreground", Color.WHITE);
        UIManager.put("MenuItem.background", Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        UIManager.put("MenuItem.foreground", Color.WHITE);
        UIManager.put("MenuBar.background", Color.decode(Colors.MENU_BACKGROUND_COLOR));
        UIManager.put("Button.background", Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("ComboBox.background", Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        UIManager.put("ComboBox.foreground", Color.WHITE);

        UIManager.put("TextField.background", Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("Label.foreground", Color.WHITE);
        UIManager.put("ToolBar.background", Color.decode(Colors.MAIN_BACKGROUND_COLOR));

        UIManager.put("Viewport.background", Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        UIManager.put("Viewport.foreground", Color.WHITE);

        UIManager.put("OptionPane.background", Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
    }

}
