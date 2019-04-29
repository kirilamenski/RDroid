package com.ansgar.rdroidpc.listeners.impl;

import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.constants.AdbKeyCode;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.entities.ScreenRecordOptions;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.listeners.DeviceActions;
import com.ansgar.rdroidpc.listeners.OnSaveScreenListener;
import com.ansgar.rdroidpc.ui.frames.VideoFrame;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Locale;

public class DeviceActionsImpl implements DeviceActions {

    private VideoFrame frame;
    private CommandExecutor executor;
    private boolean isAccelerometerDisabled;

    public DeviceActionsImpl(VideoFrame frame) {
        this.executor = new CommandExecutor();
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
    public void screenRecord(ScreenRecordOptions options, String fileName, OnSaveScreenListener listener) {
        WeakReference<Thread> weakThread = new WeakReference<>(
                new Thread(() -> {
                    String devicePath = String.format("%s%s", StringConst.DEFAULT_DEVICE_FOLDER, fileName);
                    String command = String.format(
                            Locale.ENGLISH,
                            AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.SCREEN_RECORD),
                            frame.getDevice().getDeviceId(),
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
                                    frame.getDevice().getDeviceId(), devicePath, options.getDownloadFolder()),
                            String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ADB_REMOVE_FILE),
                                    frame.getDevice().getDeviceId(), devicePath)
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
                                    frame.getDevice().getDeviceId(), devicePath),
                            String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ADB_PULL_SNAPSHOT),
                                    frame.getDevice().getDeviceId(), devicePath, path),
                            String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ADB_REMOVE_FILE),
                                    frame.getDevice().getDeviceId(), devicePath)
                    );
                })
        );
        thread.get().start();
    }

    @Override
    public void pressKeyCode(AdbKeyCode command) {
        String _command = String.format(
                AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.KEY_EVENT),
                frame.getDevice().getDeviceId(),
                command.name()
        );
        System.out.println(_command);
        executor.execute(_command);
    }

    @Override
    public void restart() {
        frame.getChimpDevice().reboot("None");
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
