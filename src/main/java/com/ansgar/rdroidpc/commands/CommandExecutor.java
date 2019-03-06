package com.ansgar.rdroidpc.commands;

import com.ansgar.rdroidpc.enums.AdbCommandEnum;

import java.io.*;
import java.util.List;

public class CommandExecutor {

    private Process process;
    private OnExecuteNextListener onExecuteNextListener;
    private onExecuteErrorListener onExecuteErrorListener;
    private OnFinishExecuteListener onFinishExecuteListener;

    public CommandExecutor() {

    }

    public CommandExecutor(OnExecuteNextListener onExecuteNextListener,
                           onExecuteErrorListener onExecuteErrorListener,
                           OnFinishExecuteListener onFinishExecuteListener) {
        this.onExecuteNextListener = onExecuteNextListener;
        this.onExecuteErrorListener = onExecuteErrorListener;
        this.onFinishExecuteListener = onFinishExecuteListener;
    }

    public void execute(AdbCommandEnum command) {
        execute(command.getCommand());
    }

    public void execute(String command) {
        System.out.println(command);
        try {
            process = Runtime.getRuntime().exec(command);

            executeInputStream();
            executeErrorStream();

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
        Process process = Runtime.getRuntime().exec(command);
        return process.getInputStream();
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
            System.out.println(line);
        }
    }

    public void destroy() {
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

    public void setOnExecuteNextListener(OnExecuteNextListener onExecuteNextListener) {
        this.onExecuteNextListener = onExecuteNextListener;
    }

    public void setOnExecuteErrorListener(CommandExecutor.onExecuteErrorListener onExecuteErrorListener) {
        this.onExecuteErrorListener = onExecuteErrorListener;
    }

    public void setOnFinishExecuteListener(OnFinishExecuteListener onFinishExecuteListener) {
        this.onFinishExecuteListener = onFinishExecuteListener;
    }

    public interface OnExecuteNextListener {
        void onNext(String line);
    }

    public interface onExecuteErrorListener {
        void onError(Throwable error);
    }

    public interface OnFinishExecuteListener {
        void onFinish(StringBuilder result);
    }

}