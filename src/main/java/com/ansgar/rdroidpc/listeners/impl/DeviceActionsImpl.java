package com.ansgar.rdroidpc.listeners.impl;

import com.android.chimpchat.core.IChimpImage;
import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.constants.SharedValuesKey;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.listeners.DeviceActions;
import com.ansgar.rdroidpc.ui.frames.VideoFrame;
import com.ansgar.rdroidpc.utils.SharedValues;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
    public void screenCapture() {
        String path = "/home/kirill/" + System.currentTimeMillis() + ".png";
        IChimpImage image = frame.getChimpDevice().takeSnapshot();
        boolean saved = image.writeToFile(path, "png");
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
//        System.out.println("Finish: " + result.toString());
    }

    @Override
    public void onError(Throwable error) {
//        System.out.println("Error: " + error);
    }

    private List<String> getCmds(String filePath) {
        List<String> cmd = new ArrayList<>();
        String adbPath = SharedValues.get(SharedValuesKey.ADB_PATH, "");
        if (!adbPath.isEmpty()) cmd.add(adbPath);
        else cmd.add("adb");
        cmd.add("-s");
        cmd.add(frame.getDevice().getDeviceId());
        cmd.add("exec-out");
        cmd.add("screencap");
        cmd.add("-p");
        cmd.add(">");
        cmd.add("'");
        cmd.add(filePath);
        cmd.add("'");
        return cmd;
    }
}
