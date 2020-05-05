package com.ansgar.rdroidpc.ui.frames;

import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.ui.components.ButtonsPanel;
import com.ansgar.rdroidpc.ui.components.OptionDialog;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class PackageInfoPanel extends BasePanel implements ButtonsPanel.OnButtonPanelListener {

    private String deviceId;
    private String packageName;

    public PackageInfoPanel(String deviceId, Rectangle rectangle, String title) {
        super(rectangle, title);
        this.deviceId = deviceId;
        this.packageName = title;
        createPanel();
    }

    private void createPanel() {
        setLayout(new BorderLayout());
        add(getScrollPane(getInfoContainer()));
    }

    private JPanel getInfoContainer() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(getTitleComponent());
        panel.add(getButtonsPanel());
        return panel;
    }

    private JScrollPane getScrollPane(JPanel scrollablePanel) {
        JScrollPane scrollPane = new JScrollPane(scrollablePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBackground(Color.CYAN);
        scrollPane.setPreferredSize(new Dimension(getWidth(), getHeight()));
        return scrollPane;
    }

    private JLabel getTitleComponent() {
        JLabel title = new JLabel(packageName, SwingConstants.CENTER);
        title.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        title.setMaximumSize(new Dimension(getWidth(), 100));
        title.setPreferredSize(new Dimension(getWidth(), 100));
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
        buttonsPanel.setBounds(0, 0, getWidth(), 50);
        buttonsPanel.setIconSize(30, 30);
        buttonsPanel.setItemClickListener(this);
        buttonsPanel.createPanel();
        return buttonsPanel;
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
