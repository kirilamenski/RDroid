package com.ansgar.rdroidpc.listeners.impl;

import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.listeners.DeviceActions;
import com.ansgar.rdroidpc.ui.frames.VideoFrame;
import com.ansgar.rdroidpc.utils.DateUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class DeviceActionsImpl implements DeviceActions, CommandExecutor.OnExecuteNextListener,
        CommandExecutor.OnFinishExecuteListener, CommandExecutor.onExecuteErrorListener {

    private VideoFrame frame;
    private CommandExecutor executor;
    private boolean isAccelerometerDisabled;

    public DeviceActionsImpl(VideoFrame frame) {
        this.executor = new CommandExecutor(this, this, this);
        this.frame = frame;
    }

    @Override
    public InputStream getInputStream(String command) throws IOException {
        return executor.getInputStream(command);
    }

    @Override
    public void disableAccelerometer(int disable) {
        executor.execute(String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ACCELEROMETER_ENABLE),
                frame.getDevice().getDeviceId(), disable));
    }

    @Override
    public void changeOrientation(int orientation) {
        if (!isAccelerometerDisabled) {
            disableAccelerometer(0);
            isAccelerometerDisabled = true;
        }
        executor.execute(String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ROTATE_DEVICE),
                frame.getDevice().getDeviceId(), orientation));
    }

    @Override
    public void screenRecord(int width, int height, int time, int bitRate) {

    }

    @Override
    public void screenCapture(String fileName, String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String devicePath = String.format("%s%s", "/sdcard/", fileName);
        WeakReference<Thread> thread = new WeakReference<>(
                new Thread(() -> {
                    executor.execute(String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ADB_TAKE_SNAPSHOT),
                            frame.getDevice().getDeviceId(), devicePath));
                    executor.execute(String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ADB_PULL_SNAPSHOT),
                            frame.getDevice().getDeviceId(), devicePath, path));
                    executor.execute(String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ADB_REMOVE_FILE),
                            frame.getDevice().getDeviceId(), devicePath));
                })
        );
        thread.get().start();
    }

    @Override
    public void destroy() {
        if (executor != null) executor.destroy();
    }

    @Override
    public void onNext(String line) {
        System.out.println(line);
    }

    @Override
    public void onFinish(StringBuilder result) {
        System.out.println("Finish: " + result.toString());
    }

    @Override
    public void onError(Throwable error) {
        System.out.println("Error: " + error);
    }
}