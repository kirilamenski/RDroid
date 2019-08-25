package com.ansgar.rdroidpc.commands.tasks;

import com.ansgar.rdroidpc.commands.CommandExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public abstract class BaseCommandTask extends TimerTask implements CommandExecutor.OnFinishExecuteListener,
        CommandExecutor.OnExecuteErrorListener, CommandExecutor.OnExecuteNextListener {

    private Timer timer;
    private CommandExecutor executor;

    public BaseCommandTask() {
        this.executor = new CommandExecutor();
        this.timer = new Timer();
        executor.setOnFinishExecuteListener(this);
        executor.setOnExecuteErrorListener(this);
        executor.setOnExecuteNextListener(this);
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
        executor.execute(command());
    }

    @Override
    public void onError(Throwable error) {
        stop();
    }

    @Override
    public void onFinish(StringBuilder result) {

    }

    @Override
    public void onNext(String line) {

    }

    @NotNull
    public abstract String command();

}
