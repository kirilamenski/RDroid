package com.ansgar.rdroidpc.ui.frames;

import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.ui.components.ButtonsPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class PackageInfoPanel extends BasePanel implements ButtonsPanel.OnButtonPanelListener {

    private String deviceId;

    public PackageInfoPanel(String deviceId, Rectangle rectangle, String title) {
        super(rectangle, title);
        this.deviceId = deviceId;
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
        JLabel title = new JLabel(frame.getTitle(), SwingConstants.CENTER);
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
        String adbCommand = "";
        switch (position) {
            case 0:
                adbCommand = AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.UN_INSTALL_APP);
                break;
            case 1:
                adbCommand = AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.CLEAR_APP_DATA);
                break;
            case 2:
                adbCommand = AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.OPEN_APP);
                break;
        }
        String command = String.format(Locale.ENGLISH, adbCommand, deviceId, frame.getTitle());
        executeAdbCommand(command);
    }

    private void executeAdbCommand(String command) {
        CommandExecutor executor = new CommandExecutor();
        executor.execute(command);
        executor.destroy();
    }
}
