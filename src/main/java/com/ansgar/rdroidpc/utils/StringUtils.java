package com.ansgar.rdroidpc.utils;

import com.ansgar.rdoidpc.constants.AdbCommandEnum;
import com.ansgar.rdoidpc.entities.Device;

import java.nio.charset.Charset;
import java.util.Locale;

public class StringUtils {

    public static String getScreenRecordCommand(Device device, int bitRate, int count) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(Locale.ENGLISH, AdbCommandEnum.ADB_SHELL.getCommand(), device.getDeviceId()))
                .append(" ");
        String option = String.format(Locale.ENGLISH, AdbCommandEnum.SCREEN_RECORD.getCommand(),
                bitRate, device.getHeight(), device.getWidth());

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
