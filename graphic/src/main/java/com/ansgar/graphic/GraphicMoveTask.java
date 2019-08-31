package com.ansgar.graphic;

import java.util.Timer;
import java.util.TimerTask;

public class GraphicMoveTask extends TimerTask {

    private Timer timer;
    private GraphicMouseListener.OnDraggedListener listener;
    private int offset;

    public GraphicMoveTask(GraphicMouseListener.OnDraggedListener listener) {
        this.timer = new Timer();
        this.listener = listener;
    }

    public void start(int delay, int period) {
        timer.schedule(this, delay, period);
    }

    @Override
    public void run() {
        offset++;
//        mouseEventListener.setxLastPos(offset);
        listener.onDragged(offset);
    }

    public void destroy() {
        timer.cancel();
        cancel();
    }
}
