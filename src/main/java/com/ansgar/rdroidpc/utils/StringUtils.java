package com.ansgar.rdroidpc.utils;

import com.ansgar.rdroidpc.constants.AdbCommandEnum;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.entities.DeviceOption;

import java.nio.charset.Charset;
import java.util.Locale;

public class StringUtils {

    public static String getScreenRecordCommand(Device device, int bitRate, int count) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                String.format(
                        Locale.ENGLISH,
                        AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ADB_SHELL),
                        device.getDeviceId()
                )
        ).append(" ");
        String option = String.format(
                Locale.ENGLISH,
                AdbCommandEnum.SCREEN_RECORD.getCommand(),
                bitRate,
                device.getHeight(),
                device.getWidth()
        );

        for (int i = 0; i <= count; i++) {
            stringBuilder.append(option);
            if (i != count) stringBuilder.append("; ");
        }

        return stringBuilder.toString();
    }

    public static String getScreenRecordCommand(DeviceOption deviceOption, int count) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                String.format(
                        Locale.ENGLISH,
                        AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ADB_SHELL),
                        deviceOption.getDeviceId()
                )
        ).append(" ");
        String option = String.format(
                Locale.ENGLISH,
                AdbCommandEnum.SCREEN_RECORD.getCommand(),
                deviceOption.getBitRate(),
                deviceOption.getHeight(),
                deviceOption.getWidth()
        );

        for (int i = 0; i <= count; i++) {
            stringBuilder.append(option);
            if (i != count) stringBuilder.append("; ");
        }

        return stringBuilder.toString();
    }


    public static String getEncodedString(char ch) {
        byte[] bytes = String.valueOf(ch).getBytes();
        return new String(bytes, Charset.forName("UTF-8"));
    }

}
