package com.ansgar.rdroidpc.utils;

import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.enums.OrientationEnum;
import com.ansgar.rdroidpc.listeners.OnDeviceOrientationListener;

import java.util.Timer;
import java.util.TimerTask;

public class OrientationUtil extends TimerTask implements CommandExecutor.OnFinishExecuteListener {

    private Timer timer;
    private CommandExecutor executor;
    private String command;
    private OnDeviceOrientationListener listener;

    private OrientationEnum prevOrientation;

    public OrientationUtil(Device device, OnDeviceOrientationListener listener) {
        this.executor = new CommandExecutor();
        this.timer = new Timer();
        this.command = String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ORIENTATION),
                device.getDeviceId());
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
        String[] strings = result.toString().trim().split(" ");
        OrientationEnum orientationEnum = OrientationEnum.Companion.getFromValue(strings[strings.length - 1]);
        if (listener != null && prevOrientation != orientationEnum) {
            listener.onOrientationChanged(orientationEnum);
            prevOrientation = orientationEnum;
        }
    }

}
