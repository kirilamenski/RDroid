package com.ansgar.rdroidpc.utils;

import com.ansgar.rdroidpc.commands.tasks.BaseCommandTask;
import com.ansgar.rdroidpc.entities.DumpsysModel;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.enums.DumpsysParserEnums;
import com.ansgar.rdroidpc.listeners.OnDumpsysListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DumpsysCommandTask extends BaseCommandTask {

    private String deviceId, packageName;
    @Nullable
    private OnDumpsysListener listener;
    private DumpsysModel dumpsysModel = new DumpsysModel();

    @Override
    public void onFinish(StringBuilder result) {
        if (listener != null) {
//            DumpsysParserEnums.Companion.parse(dumpsysModel, result);
            listener.getDumpsys(dumpsysModel);
        }
    }

    @Override
    public void onNext(String line) {
        DumpsysParserEnums.Companion.parse(dumpsysModel, line);
    }

    @NotNull
    @Override
    public String command() {
        return String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.DUMPSYS),
                deviceId, packageName);
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setListener(@Nullable OnDumpsysListener listener) {
        this.listener = listener;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
