package com.ansgar.rdroidpc.ui.components.graphicpanel;

import com.ansgar.rdroidpc.entities.DumpsysModel;
import com.ansgar.rdroidpc.entities.ProfileData;

import java.awt.*;

public class DumpsysViewHolder extends GraphicViewHolder<DumpsysModel> {

    @Override
    protected void draw(Graphics2D g2d, int position) {
        for (int i = 0; i < model.getProfileData().size(); i++) {
            ProfileData data = model.getProfileData().get(i);
            int frameLatency = (int) ((data.getFrameCompleted() - data.getIntendedVsync()) / 1000000);
            Color color = frameLatency > 16 ? Color.RED : Color.GREEN;
            g2d.setColor(color);
            int x = (i + 20) + (position * model.getProfileData().size()) + panel.getLeftMargin();
            int panelHeight = panel.getHeight() - panel.getBottomMargin();
            int frameLatencyHeight = panelHeight - frameLatency * 10;
            System.out.print("Frame: " + frameLatency + ", height: " + frameLatencyHeight + " height: " + panelHeight);
            if (frameLatencyHeight > panelHeight) frameLatencyHeight = panelHeight;
            g2d.drawLine(x, panel.getHeight() - panel.getBottomMargin(), x, frameLatencyHeight);
        }
        System.out.println();
    }
}
