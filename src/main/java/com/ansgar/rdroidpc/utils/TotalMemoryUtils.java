package com.ansgar.rdroidpc.utils;

import java.util.Timer;
import java.util.TimerTask;

public class TotalMemoryUtils extends TimerTask {
    private Timer timer;

    public TotalMemoryUtils() {
        this.timer = new Timer();
    }

    private void showMemoryUsage() {
        long bytes = Runtime.getRuntime().totalMemory();
        System.out.println(String.valueOf("Total memory: " + (bytes / 1000000.0) + " Mb"));
    }

    public void start(long delay, long period) {
        timer.schedule(this, delay, period);
    }

    public void stop() {
        if (timer != null) timer.cancel();
        cancel();
    }

    @Override
    public void run() {
        showMemoryUsage();
    }
}
