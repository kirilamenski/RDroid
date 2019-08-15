package com.ansgar.rdroidpc.commands.tasks;

import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.enums.OrientationEnum;
import com.ansgar.rdroidpc.listeners.OnDeviceOrientationListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OrientationCommandTask extends BaseCommandTask {

    private String deviceId;
    @Nullable
    private OnDeviceOrientationListener listener;
    private OrientationEnum prevOrientation;

    @Override
    public void onFinish(StringBuilder result) {
        String[] strings = result.toString()
                .trim()
                .split(" ");
        OrientationEnum orientationEnum = OrientationEnum.Companion
                .getFromValue(strings[strings.length - 1]);
        if (listener != null && prevOrientation != orientationEnum) {
            listener.onOrientationChanged(orientationEnum);
            prevOrientation = orientationEnum;
        }
    }

    @NotNull
    @Override
    public String command() {
        return String.format(
                AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ORIENTATION),
                deviceId
        );
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setListener(@Nullable OnDeviceOrientationListener listener) {
        this.listener = listener;
    }
}
