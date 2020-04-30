package com.ansgar.rdroidpc.ui.components;

import com.ansgar.rdroidpc.constants.Colors;
import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.entities.ComponentProperties;
import com.ansgar.rdroidpc.listeners.SimpleMouseListener;
import com.ansgar.rdroidpc.listeners.SimpleTextChangeListener;
import com.ansgar.rdroidpc.managers.AppPackagesManager;
import com.ansgar.rdroidpc.ui.frames.BasePanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PackageManagerPanel extends BasePanel {

    private String deviceId;
    private List<String> allPackages, foundedPackages;
    private JScrollPane scrollPane;

    public PackageManagerPanel(String deviceId, Rectangle rectangle, String title) {
        super(rectangle, title, true);
        this.deviceId = deviceId;
        this.foundedPackages = new ArrayList<>();
        new SpinnerDialog(this) {
            @Override
            public void doInBack() {
                AppPackagesManager appPackagesManager = new AppPackagesManager(deviceId);
                allPackages = appPackagesManager.getAllPackages();
                foundedPackages.addAll(allPackages);
                createPanel();
            }
        }.execute();
    }

    private void createPanel() {
        if (getLayout() == null) {
            setLayout(new BorderLayout());
            add(getQueryInput(), BorderLayout.PAGE_START);
        } else {
            remove(scrollPane);
        }
        JPanel packagesListPanel = getPackagesContainer();
        scrollPane = getScrollPanel(packagesListPanel);
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
    }

    private JTextField getQueryInput() {
        JTextField queryTf = new JTextField();
        queryTf.setPreferredSize(new Dimension(getWidth(), 30));
        queryTf.getDocument().addDocumentListener(new SimpleTextChangeListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updatePackagesList(queryTf.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updatePackagesList(queryTf.getText());
            }
        });
        return queryTf;
    }

    private JScrollPane getScrollPanel(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(25);
        scrollPane.setPreferredSize(new Dimension(getWidth(), getHeight() - DimensionConst.DEVICE_CONTAINER_HEIGHT));
        return scrollPane;
    }

    private JPanel getPackagesContainer() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        for (String foundedPackage : foundedPackages) {
            container.add(getPackageName(foundedPackage));
        }
        return container;
    }

    private JLabel getPackageName(String packageName) {
        JLabel packageNameL = new JLabel(packageName);
        packageNameL.setOpaque(true);
        packageNameL.setMaximumSize(new Dimension(getWidth(), 20));
        packageNameL.setBackground(Color.decode(Colors.MAIN_BACKGROUND_COLOR));
        packageNameL.addMouseListener(getMouseListener(packageNameL));
        return packageNameL;
    }

    private void updatePackagesList(String query) {
        foundedPackages.clear();
        if (query != null && !query.isEmpty()) {
            for (String packageName : allPackages) {
                if (packageName.contains(query)) {
                    foundedPackages.add(packageName);
                }
            }
        } else {
            foundedPackages.addAll(allPackages);
        }
        createPanel();
    }

    private SimpleMouseListener getMouseListener(Component component) {
        SimpleMouseListener mouseListener = new SimpleMouseListener(component);
        mouseListener.setProperties(new ComponentProperties(
                Color.lightGray,
                Color.decode(Colors.MAIN_BACKGROUND_COLOR)
        ));
        return mouseListener;
    }

}

