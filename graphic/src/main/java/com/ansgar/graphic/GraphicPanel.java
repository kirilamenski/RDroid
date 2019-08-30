package com.ansgar.graphic;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GraphicPanel<GVH extends GraphicViewHolder> extends JPanel implements GraphicMouseListener.OnDraggedListener {

    private List<GVH> holders;
    private int xAxisOffset = 5, yAxisOffset = 50, xDraggedOffset, xAxisWidth;
    private int leftMargin = 30, topMargin = 10, rightMargin = 10, bottomMargin = 10;
    private boolean useGrid, autoMoving;
    private GraphicMouseListener mouseEventListener;

    public GraphicPanel() {
        holders = new ArrayList<>();
        initMouseListener();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawYAxis(g2d);
        drawXAxis(g2d);
        updateAll(g2d);
        g2d.dispose();
    }

    public abstract GVH onCreateViewHolder();

    public abstract void onBindViewHolder(GVH holder, int position);

    public int getItemSize() {
        return 0;
    }

    public void drawYAxis(Graphics2D g2d) {
        int left = leftMargin - xDraggedOffset;
        drawLine(g2d, left, topMargin, left, getHeight() - bottomMargin, Color.WHITE);
        int width = getHeight() - bottomMargin - topMargin;
        int step = yAxisOffset < 50 ? yAxisOffset * 10 : yAxisOffset;
        for (int i = yAxisOffset; i < width; i = i + step) {
            drawString(g2d, String.valueOf(xAxisOffset * (i / step)), 0 - xDraggedOffset, width - i);
            if (useGrid) {
                drawLine(
                        g2d,
                        leftMargin,
                        width - i,
                        getWidth() - rightMargin,
                        width - i,
                        Color.BLUE,
                        new BasicStroke(
                                1.0f,
                                BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_MITER,
                                10.0f,
                                new float[]{10.0f},
                                0.0f
                        )
                );
            }
        }
    }

    public void drawXAxis(Graphics2D g2d) {
        drawLine(g2d, leftMargin - xDraggedOffset, getHeight() - bottomMargin,
                getWidth() - rightMargin, getHeight() - bottomMargin, Color.WHITE);
        xAxisWidth = getWidth() - rightMargin - leftMargin + xDraggedOffset;
        int step = xAxisOffset < 50 ? xAxisOffset * 10 : xAxisOffset;
        for (int i = 0; i < xAxisWidth; i = i + step) {
            drawString(g2d, String.valueOf(xAxisOffset * (i / step)), leftMargin + i - xDraggedOffset, getHeight());
            if (useGrid && i > 0) {
                drawLine(
                        g2d,
                        leftMargin + i,
                        getHeight() - bottomMargin,
                        leftMargin + i,
                        topMargin,
                        Color.BLUE,
                        new BasicStroke(
                                1.0f,
                                BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_MITER,
                                10.0f,
                                new float[]{10.0f},
                                0.0f
                        )
                );
            }
        }
    }

    public void drawString(Graphics2D g2d, String text, int x, int y) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 10));
        g2d.drawString(text, x, y);
    }

    public void drawLine(Graphics2D g2d, int x1, int y1, int x2, int y2, Color color) {
        drawLine(g2d, x1, y1, x2, y2, color, new BasicStroke());
    }

    public void drawLine(Graphics2D g2d, int x1, int y1, int x2, int y2, Color color, Stroke stroke) {
        g2d.setStroke(stroke);
        g2d.setColor(color);
        g2d.drawLine(x1, y1, x2, y2);
    }

    private void updateAll(Graphics2D g2d) {
        for (int i = 0; i < holders.size(); i++) {
            GVH holder = holders.get(i);
            holder.draw(g2d, i);
        }
    }

    private void addItem(Graphics2D g2d, int position) {
        GVH holder = onCreateViewHolder();
        holders.add(holder);
        onBindViewHolder(holder, position);
        holder.panel = this;
        holder.draw(g2d, position);

        if (autoMoving) moveTo(position);
    }

    private void initMouseListener() {
        mouseEventListener = new GraphicMouseListener(this);
        addMouseListener(mouseEventListener);
        addMouseMotionListener(mouseEventListener);
    }

    public void notifyItemsUpdateChanged() {
        repaint();
    }

    public void notifyItemAdded() {
        addItem((Graphics2D) getGraphics(), getItemSize() - 1);
    }

    public void moveTo(int position) {
        if (position < 0 || position > holders.size() - 1) return;
        GVH holder = holders.get(position);
        int holderWidth = holder.getWidth();
        int holderX = getHolderWidthByPos(position - 1);
        if (holderX + holderWidth > getWidth()) {
            int delay = holderX + holderWidth + xDraggedOffset;
            mouseEventListener.setxLastPos(delay);
            onDragged(delay);
        }
    }

    public void setxAxisOffset(int xAxisOffset) {
        this.xAxisOffset = xAxisOffset;
    }

    public void setyAxisOffset(int yAxisOffset) {
        this.yAxisOffset = yAxisOffset;
    }

    public void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
    }

    public void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
    }

    public void setRightMargin(int rightMargin) {
        this.rightMargin = rightMargin;
    }

    public int getxAxisOffset() {
        return xAxisOffset;
    }

    public int getyAxisOffset() {
        return yAxisOffset;
    }

    public int getLeftMargin() {
        return leftMargin;
    }

    public int getTopMargin() {
        return topMargin;
    }

    public int getRightMargin() {
        return rightMargin;
    }

    public int getBottomMargin() {
        return bottomMargin;
    }

    public void setBottomMargin(int bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    public boolean isAutoMoving() {
        return autoMoving;
    }

    public void setAutoMoving(boolean autoMoving) {
        this.autoMoving = autoMoving;
    }

    public int getHolderWidthByPos(int position) {
        int width = leftMargin - xDraggedOffset;
        if (position >= 0 && position < holders.size()) {
            width = 10 + holders.get(position).getWidth();
        }
        return width;
    }

    @Override
    public void onDragged(int offset) {
        if (offset < 0) offset = 0;
        xDraggedOffset = offset;
        repaint();
    }
}
