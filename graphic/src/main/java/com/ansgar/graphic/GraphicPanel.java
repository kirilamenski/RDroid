package com.ansgar.graphic;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GraphicPanel<GVH extends GraphicViewHolder> extends JPanel implements GraphicMouseListener.OnDraggedListener {

    private List<GVH> holders;
    private int xAxisOffset = 5, yAxisOffset = 50, xDraggedOffset, xAxisWidth;
    private int leftMargin = 30, topMargin = 10, rightMargin = 10, bottomMargin = 10;
    private boolean useXGrid, useYGrid;
    private GraphicMouseListener mouseEventListener;
    private GraphicMoveTask moveTask;

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
            if (useYGrid) {
                drawLine(
                        g2d,
                        leftMargin,
                        width - i,
                        getWidth() - rightMargin,
                        width - i,
                        Color.GRAY,
                        new BasicStroke(
                                0.5f,
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
            if (useXGrid && i > 0) {
                drawLine(
                        g2d,
                        leftMargin + i - xDraggedOffset,
                        getHeight() - bottomMargin,
                        leftMargin + i - xDraggedOffset,
                        topMargin,
                        Color.GRAY,
                        new BasicStroke(
                                0.5f,
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
    }

    private void initMouseListener() {
        mouseEventListener = new GraphicMouseListener(this);
        addMouseListener(mouseEventListener);
        addMouseMotionListener(mouseEventListener);
    }

    public void startAutoMoving() {
        if (moveTask == null) {
            moveTask = new GraphicMoveTask(this);
            moveTask.start(6000, 25);
        }
    }

    public void notifyItemsUpdateChanged() {
        repaint();
    }

    public void notifyItemAdded() {
        addItem((Graphics2D) getGraphics(), getItemSize() - 1);
    }

    public void destroy() {
        if (moveTask != null) moveTask.cancel();
        moveTask = null;
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

    public void setUseXGrid(boolean useXGrid) {
        this.useXGrid = useXGrid;
    }

    public void setUseYGrid(boolean useYGrid) {
        this.useYGrid = useYGrid;
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
