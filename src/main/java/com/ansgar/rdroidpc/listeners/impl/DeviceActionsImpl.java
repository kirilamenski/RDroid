package com.ansgar.rdroidpc.listeners.impl;

import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.listeners.DeviceActions;

import java.io.IOException;
import java.io.InputStream;

public class DeviceActionsImpl implements DeviceActions, CommandExecutor.OnExecuteNextListener,
        CommandExecutor.OnFinishExecuteListener, CommandExecutor.onExecuteErrorListener {

    private Device device;
    private CommandExecutor executor;
    private boolean isAccelerometerDisabled;

    public DeviceActionsImpl(Device device) {
        this.executor = new CommandExecutor(this, this, this);
        this.device = device;
    }

    @Override
    public InputStream getInputStream(String command) throws IOException {
        return executor.getInputStream(command);
    }

    @Override
    public void disableAccelerometer(int disable) {
        executor.execute(String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ACCELEROMETER_ENABLE), device.getDeviceId(), disable));
    }

    @Override
    public void changeOrientation(int orientation) {
        if (!isAccelerometerDisabled) {
            disableAccelerometer(0);
            isAccelerometerDisabled = true;
        }
        executor.execute(String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ROTATE_DEVICE), device.getDeviceId(), orientation));
    }

    @Override
    public void screenRecord(int width, int height, int time, int bitRate) {

    }

    @Override
    public void screenCapture(int width, int height) {

    }

    @Override
    public void destroy() {
        if (executor != null) executor.destroy();
    }

    @Override
    public void onNext(String line) {

    }

    @Override
    public void onFinish(StringBuilder result) {

    }

    @Override
    public void onError(Throwable error) {

    }
}
