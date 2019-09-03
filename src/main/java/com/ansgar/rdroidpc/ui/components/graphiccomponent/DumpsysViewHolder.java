package com.ansgar.rdroidpc.ui.components.graphiccomponent;

import com.ansgar.graphic.GraphicViewHolder;
import com.ansgar.rdroidpc.constants.Colors;
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
            if (data.getFlags() == 0) {
                int frameLatency = (int) ((data.getFrameCompleted() - data.getIntendedVsync()) / 1000000);
                String dumpsysColor = frameLatency > 16 ? Colors.ALLERT_DUMPSYS_COLOR : Colors.DUMPSYS_COLOR;
                Color color = Color.decode(dumpsysColor);
                g2d.setColor(color);
                int x = i + panel.getHolderEndXPosByIndex(position - 1);
                int panelHeight = panel.getHeight() - panel.getBottomMargin();
                int frameLatencyHeight = panelHeight - frameLatency * 10;
                if (frameLatencyHeight > panelHeight) frameLatencyHeight = panelHeight;
                g2d.drawLine(x, panel.getHeight() - panel.getBottomMargin(), x, frameLatencyHeight);

                if (i == 0) setStartX(x);
                if (i == profileDataList.size() - 1) setEndX(x);
            }
        }
    }
}
