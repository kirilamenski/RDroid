package com.ansgar.rdroidpc.utils;

import com.ansgar.rdroidpc.commands.tasks.BaseCommandTask;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.listeners.OnDumpsysListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DumpsysCommandTask extends BaseCommandTask {

    private String deviceId;
    @Nullable
    private OnDumpsysListener listener;

    @Override
    public void onFinish(StringBuilder result) {
        if (listener != null) listener.getDumpsys(result);
    }

    @NotNull
    @Override
    public String command() {
        return String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.DUMPSYS),
                deviceId, "com.fishbowlmedia.fishbowl.dev");
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setListener(@Nullable OnDumpsysListener listener) {
        this.listener = listener;
    }
}
