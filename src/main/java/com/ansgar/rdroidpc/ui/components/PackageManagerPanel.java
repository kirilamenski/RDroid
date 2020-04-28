package com.ansgar.rdroidpc.ui.components;

import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.listeners.SimpleTextChangeListener;
import com.ansgar.rdroidpc.managers.AppPackagesManager;
import com.ansgar.rdroidpc.ui.frames.BasePanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.util.List;

public class PackageManagerPanel extends BasePanel {

    private String deviceId;
    private java.util.List<String> allPackages;
    private List<String> foundedPackages;

    public PackageManagerPanel(String deviceId, Rectangle rectangle, String title) {
        super(rectangle, title, true);
        this.deviceId = deviceId;
        new SpinnerDialog(this) {
            @Override
            public void doInBack() {
                AppPackagesManager appPackagesManager = new AppPackagesManager(deviceId);
                allPackages = appPackagesManager.getAllPackages("");
                createPanel();
            }
        }.execute();
    }

    private void createPanel() {
        setLayout(new BorderLayout());
        add(getQueryInput(), BorderLayout.PAGE_START);
        add(getScrollPanel(getPackagesContainer()), BorderLayout.PAGE_END);
        revalidate();
    }

    @NotNull
    private JTextField getQueryInput() {
        JTextField queryTf = new JTextField();
        queryTf.setPreferredSize(new Dimension(getWidth(), DimensionConst.DEVICE_CONTAINER_HEIGHT));
        queryTf.getDocument().addDocumentListener(new SimpleTextChangeListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                //TODO make query after input. Use rx to perform it after delay
            }
        });
        return queryTf;
    }

    @NotNull
    private JScrollPane getScrollPanel(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(getWidth(), getHeight() - DimensionConst.DEVICE_CONTAINER_HEIGHT));
        return scrollPane;
    }

    @NotNull
    private JPanel getPackagesContainer() {
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        for (int i = 0; i < allPackages.size(); i++) {
            container.add(getPackageName(allPackages.get(i), i + 1), gbc);
        }
        return container;
    }

    @NotNull
    private JButton getPackageName(String packageName, int position) {
        JButton packageNameL = new JButton(packageName);
        return packageNameL;
    }
}

