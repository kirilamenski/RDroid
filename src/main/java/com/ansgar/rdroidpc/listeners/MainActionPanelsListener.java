package com.ansgar.rdroidpc.listeners;

import com.ansgar.rdroidpc.ui.components.ButtonsPanel;
import com.ansgar.rdroidpc.ui.frames.MainPanel;

public class MainActionPanelsListener implements ButtonsPanel.OnButtonPanelListener {

    private MainPanel panel;

    public MainActionPanelsListener(MainPanel panel) {
        this.panel = panel;
    }

    @Override
    public void onActionItemClicked(int position) {
        switch (position) {
            case 0:
                panel.executeAdbDevices();
                break;
            case 1:
                panel.killServer();
                break;
        }
    }
}
