package com.ansgar.rdroidpc.ui.frames;

import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.ui.components.ButtonsPanel;
import com.ansgar.rdroidpc.ui.components.OptionDialog;
import com.ansgar.rdroidpc.ui.components.SpinnerDialog;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class PackageInfoPanel extends BasePanel implements ButtonsPanel.OnButtonPanelListener {

    private String deviceId;
    private String packageName;

    public PackageInfoPanel(String deviceId, Rectangle rectangle, String title) {
        super(rectangle, title, true);
        this.deviceId = deviceId;
        this.packageName = title;
        new SpinnerDialog(this) {
            @Override
            public void doInBack() {
                CommandExecutor executor = new CommandExecutor();
                executor.setOnFinishExecuteListener(result -> {
                    createPanel(result.toString());
                    executor.destroy();
                });
                executor.execute(String.format(
                        Locale.ENGLISH,
                        AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.PACKAGE_INFO),
                        deviceId,
                        packageName
                ));
            }
        }.execute();
    }

    private void createPanel(String packageInfo) {
        setLayout(null);
        add(getInfoContainer());
        add(getScrollPane(getPackageInformation(packageInfo)));
        updateUI();
    }

    private JPanel getInfoContainer() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, getWidth(), 100);
        panel.add(getTitleComponent());
        panel.add(getButtonsPanel());
        return panel;
    }

    private JScrollPane getScrollPane(JTextArea scrollablePanel) {
        JScrollPane scrollPane = new JScrollPane(scrollablePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(25);
        scrollPane.setBounds(0, 100, getWidth(), getHeight());
        scrollPane.setPreferredSize(new Dimension(getWidth(), getHeight()));
        return scrollPane;
    }

    private JLabel getTitleComponent() {
        JLabel title = new JLabel(packageName, SwingConstants.CENTER);
        title.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        title.setBounds(0, 0, getWidth(), 50);
        return title;
    }

    private ButtonsPanel getButtonsPanel() {
        ButtonsPanel buttonsPanel = new ButtonsPanel();
        buttonsPanel.setIcons(
                "icons/ic_un_install_64.png",
                "icons/ic_clear_cache_64.png",
                "icons/ic_home_64.png",
                "icons/ic_settings_2_64.png"
        );
        buttonsPanel.setToolTips(
                StringConst.UN_INSTALL,
                StringConst.CLEAR_CACHE,
                StringConst.OPEN_APP,
                StringConst.OPEN_APP_SETTINGS
        );
        buttonsPanel.setBounds(0, 50, getWidth(), 50);
        buttonsPanel.setIconSize(30, 30);
        buttonsPanel.setItemClickListener(this);
        buttonsPanel.createPanel();
        return buttonsPanel;
    }

    private JTextArea getPackageInformation(String packageInfo) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setText(packageInfo);
        textArea.setBounds(0, 100, getWidth(), getHeight() - 100);
        return textArea;
    }

    @Override
    public void onActionItemClicked(int position) {
        switch (position) {
            case 0:
                showWarningDialog(
                        getCommand(AdbCommandEnum.UN_INSTALL_APP),
                        StringConst.UN_INSTALL.toLowerCase(),
                        packageName
                );
                break;
            case 1:
                showWarningDialog(
                        getCommand(AdbCommandEnum.CLEAR_APP_DATA),
                        StringConst.CLEAR_CACHE.toLowerCase(),
                        packageName
                );
                break;
            case 2:
                executeAdbCommand(getCommand(AdbCommandEnum.OPEN_APP));
                break;
        }
    }

    private String getCommand(AdbCommandEnum adbCommand) {
        return String.format(
                Locale.ENGLISH,
                AdbCommandEnum.Companion.getCommandValue(adbCommand),
                deviceId,
                packageName
        );
    }

    private void showWarningDialog(String command, String action, String packageName) {
        String dialogTitle = String.format(
                StringConst.CLEAR_APP_DATA_WARNING,
                action,
                packageName
        );
        int result = new OptionDialog()
                .setDialogTitle(dialogTitle)
                .setMainTitle("")
                .showDialog(this, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result == 0) {
            executeAdbCommand(command);
        }
    }

    private void executeAdbCommand(String command) {
        CommandExecutor executor = new CommandExecutor();
        executor.execute(command);
        executor.destroy();
    }
}
