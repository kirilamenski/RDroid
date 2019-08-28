package com.ansgar.rdroidpc.commands;

import com.ansgar.rdroidpc.enums.AdbCommandEnum;

import java.io.*;
import java.util.List;

public class CommandExecutor {

    private Process process, videoProcess;
    private OnExecuteNextListener onExecuteNextListener;
    private OnExecuteErrorListener onExecuteErrorListener;
    private OnFinishExecuteListener onFinishExecuteListener;

    public CommandExecutor() {

    }

    public CommandExecutor(OnExecuteNextListener onExecuteNextListener,
                           OnExecuteErrorListener onExecuteErrorListener,
                           OnFinishExecuteListener onFinishExecuteListener) {
        this.onExecuteNextListener = onExecuteNextListener;
        this.onExecuteErrorListener = onExecuteErrorListener;
        this.onFinishExecuteListener = onFinishExecuteListener;
    }

    public void execute(AdbCommandEnum command) {
        execute(command.getCommand());
    }

    public void execute(String command) {
        try {
            process = Runtime.getRuntime().exec(command);
            executeErrorStream();
            executeInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute(List<String> cmd) {
        try {
            ProcessBuilder builder = new ProcessBuilder(cmd);
            builder.redirectErrorStream(true);
            process = builder.start();

            executeInputStream();
            executeErrorStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream getInputStream(AdbCommandEnum adbCommandEnum) throws IOException {
        return getInputStream(adbCommandEnum.getCommand());
    }

    public InputStream getInputStream(String command) throws IOException {
        videoProcess = Runtime.getRuntime().exec(command);
        return videoProcess.getInputStream();
    }

    private void executeInputStream() throws IOException {
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = inputStream.readLine()) != null) {
            stringBuilder.append(line).append("\n");
            if (onExecuteNextListener != null) onExecuteNextListener.onNext(line);
        }
        if (onFinishExecuteListener != null) onFinishExecuteListener.onFinish(stringBuilder);
    }

    private void executeErrorStream() throws IOException {
        BufferedReader errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = errorStream.readLine()) != null) {
            if (onExecuteErrorListener != null) {
                onExecuteErrorListener.onError(new Throwable(line));
            }
        }
    }

    public void destroy() {
        destroyProcess(process);
        destroyProcess(videoProcess);
    }

    public void setOnExecuteNextListener(OnExecuteNextListener onExecuteNextListener) {
        this.onExecuteNextListener = onExecuteNextListener;
    }

    public void setOnExecuteErrorListener(OnExecuteErrorListener onExecuteErrorListener) {
        this.onExecuteErrorListener = onExecuteErrorListener;
    }

    public void setOnFinishExecuteListener(OnFinishExecuteListener onFinishExecuteListener) {
        this.onFinishExecuteListener = onFinishExecuteListener;
    }

    public interface OnExecuteNextListener {
        void onNext(String line);
    }

    public interface OnExecuteErrorListener {
        void onError(Throwable error);
    }

    public interface OnFinishExecuteListener {
        void onFinish(StringBuilder result);
    }

    private void destroyProcess(Process process) {
        if (process != null) {
            OutputStream outputStream = process.getOutputStream();
            InputStream inputStream = process.getInputStream();
            InputStream errorStream = process.getErrorStream();
            try {
                outputStream.close();
                outputStream.flush();
                inputStream.close();
                errorStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            process.destroyForcibly();
        }
    }

}