package com.ansgar.rdroidpc.ui.frames;

import com.ansgar.filemanager.DesktopUtil;
import com.ansgar.rdroidpc.constants.StringConst;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;

public class AboutPanel extends BasePanel {

    public AboutPanel(Rectangle rectangle, String title) {
        super(rectangle, title);
        createPanel();
    }

    private void createPanel() {
        JLabel label = new JLabel(StringConst.INFORMATION);
        label.setBounds(10, 10, getRectangle().width, getRectangle().height);
        label.setVerticalAlignment(JLabel.TOP);

        String[] links = StringConst.Companion.getLinks();
        add(label);

        // TODO refactor
        for (int i = 0; i < links.length; i++) {
            if ((i + 1) % 2 == 0) {
                JLabel linkLabel = new JLabel(links[i]);
                linkLabel.addMouseListener(getAdapter(links[i - 1]));
                linkLabel.setBounds(10, label.getHeight() + ((i - 2) * 2) - 100, getRectangle().width, 20);
                add(linkLabel);
            }
        }

    }

    private MouseAdapter getAdapter(String link) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    DesktopUtil.browse(URI.create(link));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };
    }

}
