package com.ansgar.rdroidpc.utils;

import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.listeners.OnDumpsysListener;
import org.jetbrains.annotations.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class DumpsysUtil extends TimerTask implements CommandExecutor.OnFinishExecuteListener,
        CommandExecutor.OnExecuteErrorListener {

    private Timer timer;
    private CommandExecutor executor;
    private String command;
    @Nullable
    private OnDumpsysListener listener;

    public DumpsysUtil(String deviceId, @Nullable OnDumpsysListener listener) {
        this.executor = new CommandExecutor();
        this.timer = new Timer();
        this.command = String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.DUMPSYS),
                deviceId, "com.ansgar.motusmontis");
        this.listener = listener;
        executor.setOnFinishExecuteListener(this);
        executor.setOnExecuteErrorListener(this);
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
    public void onError(Throwable error) {
        stop();
    }

    @Override
    public void onFinish(StringBuilder result) {
        System.out.println(result.toString());
    }
}
