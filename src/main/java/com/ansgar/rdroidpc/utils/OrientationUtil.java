package com.ansgar.rdroidpc.utils;

import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.constants.AdbCommandEnum;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.enums.OrientationEnum;
import com.ansgar.rdroidpc.listeners.OnDeviceOrientationListener;

import java.util.Timer;
import java.util.TimerTask;

public class OrientationUtil extends TimerTask implements CommandExecutor.OnFinishExecuteListener {

    private Device device;
    private Timer timer;
    private TimerTask timerTask;
    private CommandExecutor executor;
    private String command;
    private OnDeviceOrientationListener listener;

    public OrientationUtil(Device device, OnDeviceOrientationListener listener) {
        this.device = device;
        this.executor = new CommandExecutor();
        this.timer = new Timer();
        this.command = String.format(AdbCommandEnum.ORIENTATION.getCommand(), device.getDeviceId());
        this.listener = listener;
        executor.setOnFinishExecuteListener(this);
    }

    public void start(long delay, long period) {
        timer.schedule(this, delay, period);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
        cancel();
    }

    @Override
    public void run() {
        executor.execute(command);
    }

    @Override
    public void onFinish(StringBuilder result) {
        OrientationEnum orientationEnum = OrientationEnum.Companion.getFromValue(result.toString().trim());
        if (listener != null && orientationEnum != null) listener.onOrientationChanged(orientationEnum);
    }
}
