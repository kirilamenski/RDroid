package com.ansgar.rdroidpc.ui.components;

import javax.swing.*;

public class OptionDialog extends JOptionPane {

    private String mainTitle;
    private String dialogTitle;

    public OptionDialog setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
        return this;
    }

    public OptionDialog setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
        return this;
    }

    public int showDialog(JComponent component, int optionType, int messageType) {
        return OptionDialog.showOptionDialog(
                component,
                dialogTitle,
                mainTitle,
                optionType,
                messageType,
                null,
                null,
                null
        );
    }
}
