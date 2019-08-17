package com.ansgar.rdroidpc.listeners.impl;

import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.commands.ResponseParserUtil;
import com.ansgar.rdroidpc.constants.AdbKeyCode;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.entities.ScreenRecordOptions;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.listeners.DeviceActions;
import com.ansgar.rdroidpc.listeners.OnSaveScreenListener;
import com.ansgar.rdroidpc.ui.frames.VideoFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DeviceActionsImpl implements DeviceActions {

    private String deviceId;
    private CommandExecutor executor;
    private boolean isAccelerometerDisabled;

    public DeviceActionsImpl() {
        this.executor = new CommandExecutor();
    }

    public DeviceActionsImpl(@NotNull String deviceId) {
        this.executor = new CommandExecutor();
        this.deviceId = deviceId;
    }

    @Override
    public List<Device> getConnectedDevices() {
        List<Device> devices = new ArrayList<>();
        executor.setOnFinishExecuteListener(result -> {
            ResponseParserUtil parserUtil = new ResponseParserUtil();
            devices.addAll(parserUtil.getDevices(result));
        });
        executor.execute(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.DEVICES));
        return devices;
    }

    @Override
    public void startServer() {
        executor.execute(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.START_SERVER));
    }

    @Override
    public void killServer() {
        executor.execute(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.KILL_SERVER));
    }

    @Override
    public boolean isDevicesConnected(String deviceId) {
        for (Device connectedDevice : getConnectedDevices()) {
            String connectedDeviceId = connectedDevice.getDeviceId();
            String deviceStatus = connectedDevice.getDeviceStatus();
            if (connectedDeviceId != null && connectedDeviceId.equals(deviceId)
                    && deviceStatus != null && deviceStatus.contains("device")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public InputStream getInputStream(String command) throws IOException {
        return executor.getInputStream(command);
    }

    @Override
    public void disableAccelerometer(int disable) {
        executor.execute(String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ACCELEROMETER_ENABLE),
                deviceId, disable));
    }

    @Override
    public void changeOrientation(int orientation) {
        if (!isAccelerometerDisabled) {
            disableAccelerometer(0);
            isAccelerometerDisabled = true;
        }
        executor.execute(String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ROTATE_DEVICE),
                deviceId, orientation));
    }

    @Override
    public void screenRecord(ScreenRecordOptions options, String fileName, OnSaveScreenListener listener) {
        WeakReference<Thread> weakThread = new WeakReference<>(
                new Thread(() -> {
                    String devicePath = String.format("%s%s", StringConst.DEFAULT_DEVICE_FOLDER, fileName);
                    String command = String.format(
                            Locale.ENGLISH,
                            AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.SCREEN_RECORD),
                            deviceId,
                            options.getBitRate(),
                            options.getTime(),
                            options.getWidth(),
                            options.getHeight(),
                            devicePath
                    );
                    executeScreenActions(
                            listener,
                            options.getDownloadFolder(),
                            command,
                            String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ADB_PULL_SNAPSHOT),
                                    deviceId, devicePath, options.getDownloadFolder()),
                            String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ADB_REMOVE_FILE),
                                    deviceId, devicePath)
                    );
                })
        );
        weakThread.get().start();
    }

    @Override
    public void screenCapture(String fileName, String path, OnSaveScreenListener listener) {
        String devicePath = String.format("%s%s", StringConst.DEFAULT_DEVICE_FOLDER, fileName);
        WeakReference<Thread> thread = new WeakReference<>(
                new Thread(() -> {
                    executeScreenActions(
                            listener,
                            path,
                            String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ADB_TAKE_SNAPSHOT),
                                    deviceId, devicePath),
                            String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ADB_PULL_SNAPSHOT),
                                    deviceId, devicePath, path),
                            String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ADB_REMOVE_FILE),
                                    deviceId, devicePath)
                    );
                })
        );
        thread.get().start();
    }

    @Override
    public void pressKeyCode(AdbKeyCode keyCode) {
        String command = String.format(
                AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.KEY_EVENT),
                deviceId,
                keyCode.name()
        );
        executor.execute(command);
    }

    @Override
    public void destroy() {
        if (executor != null) executor.destroy();
    }

    private void executeScreenActions(OnSaveScreenListener listener, String uploadFolder, String... commands) {
        WeakReference<CommandExecutor> executorWeakReference = new WeakReference<>(new CommandExecutor());
        for (int i = 0; i < commands.length; i++) {
            String command = commands[i];
            if (i == commands.length - 1) {
                executorWeakReference.get().setOnFinishExecuteListener(result -> listener.onScreenSaved(uploadFolder));
            }
            executorWeakReference.get().execute(command);
        }
        executorWeakReference.get().destroy();
        executorWeakReference.clear();
    }

}
