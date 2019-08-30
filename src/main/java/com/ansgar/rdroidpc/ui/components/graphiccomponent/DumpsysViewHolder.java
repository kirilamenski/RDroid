package com.ansgar.rdroidpc.ui.components.graphiccomponent;

import com.ansgar.graphic.GraphicViewHolder;
import com.ansgar.rdroidpc.entities.DumpsysModel;
import com.ansgar.rdroidpc.entities.ProfileData;

import java.awt.*;
import java.util.List;

public class DumpsysViewHolder extends GraphicViewHolder<DumpsysModel> {

    @Override
    protected void draw(Graphics2D g2d, int position) {
        List<ProfileData> profileDataList = model.getProfileData();
        for (int i = 0; i < profileDataList.size(); i++) {
            ProfileData data = profileDataList.get(i);
            int frameLatency = (int) ((data.getFrameCompleted() - data.getIntendedVsync()) / 1000000);
            Color color = frameLatency > 16 ? Color.RED : Color.GREEN;
            g2d.setColor(color);
            int x = i + panel.getHolderWidthByPos(position - 1);
            int panelHeight = panel.getHeight() - panel.getBottomMargin();
            int frameLatencyHeight = panelHeight - frameLatency * 10;
            if (frameLatencyHeight > panelHeight) frameLatencyHeight = panelHeight;
            g2d.drawLine(x, panel.getHeight() - panel.getBottomMargin(), x, frameLatencyHeight);

            if (i == profileDataList.size() - 1) setWidth(x);
        }
    }
}
