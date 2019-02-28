package com.ansgar.rdroidpc.utils;

import com.ansgar.rdroidpc.constants.Colors;

import javax.swing.*;
import java.awt.*;

public class AppUiCustomizationUtil {

    public static void customizeApp() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
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
        UIManager.put("TextField.caretForeground", Color.WHITE);

        UIManager.put("Label.foreground", Color.WHITE);

        UIManager.put("ToolBar.background", Color.decode(Colors.MAIN_BACKGROUND_COLOR));

        UIManager.put("Viewport.background", Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        UIManager.put("Viewport.foreground", Color.WHITE);

        UIManager.put("OptionPane.background", Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);

        UIManager.put("FormattedTextField.background", Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        UIManager.put("FormattedTextField.foreground", Color.WHITE);

        UIManager.put("ToggleButton.background", Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        UIManager.put("ToggleButton.select", Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        UIManager.put("ToggleButton.foreground", Color.WHITE);

        UIManager.put("ToolTip.background", Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        UIManager.put("ToolTip.foreground", Color.WHITE);
    }

}
