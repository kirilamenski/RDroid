package com.ansgar.rdroidpc.ui.components.graphiccomponent;

import com.ansgar.rdroidpc.entities.DumpsysModel;
import com.ansgar.graphic.GraphicPanel;

import java.util.ArrayList;
import java.util.List;

public class DumpsysGraphicComponent extends GraphicPanel<DumpsysViewHolder> {

    private List<DumpsysModel> items = new ArrayList<>();

    public DumpsysGraphicComponent() {

    }

    @Override
    public DumpsysViewHolder onCreateViewHolder() {
        return new DumpsysViewHolder();
    }

    @Override
    public void onBindViewHolder(DumpsysViewHolder holder, int position) {
        holder.setModel(items.get(position));
    }

    @Override
    public int getItemSize() {
        return items.size();
    }

    public void addItem(DumpsysModel model) {
        items.add(model);
        notifyItemAdded();
    }

}
