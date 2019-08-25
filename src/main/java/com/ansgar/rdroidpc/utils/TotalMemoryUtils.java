package com.ansgar.rdroidpc.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Timer;
import java.util.TimerTask;

public class TotalMemoryUtils extends TimerTask {
    private Timer timer;

    public TotalMemoryUtils() {
        this.timer = new Timer();
    }

    private void showMemoryUsage() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        long bytes = Runtime.getRuntime().totalMemory();
        String builder = "Total memory: " +
                (bytes / 1000000.0) +
                " Mb \n " +
                operatingSystemMXBean.getName() +
                "(" +
                operatingSystemMXBean.getVersion() +
                ", " +
                operatingSystemMXBean.getArch() +
                "),\n Available Processors: " +
                operatingSystemMXBean.getAvailableProcessors() +
                ", Average system load: " +
                operatingSystemMXBean.getSystemLoadAverage();
        System.out.println(builder);
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
