package com.ansgar.rdroidpc.utils;

import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.entities.Option;

import java.nio.charset.Charset;
import java.util.Locale;

public class StringUtils {

    public static String getScreenRecordCommand(Device device, int count) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(
                Locale.ENGLISH,
                AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.ADB_SHELL),
                device.getDeviceId()
        ))
                .append(" ");
        Option option = device.getOption();
        int _bitRate = option != null ? option.getBitRate() : 4;
        int height = option != null ? option.getHeight() : device.getHeight();
        int width = option != null ? option.getWidth() : device.getWidth();
        String command = String.format(
                Locale.ENGLISH,
                AdbCommandEnum.SCREEN_STREAMING.getCommand(),
                _bitRate,
                height,
                width
        );

        for (int i = 0; i <= count; i++) {
            stringBuilder.append(command);
            if (i != count) stringBuilder.append("; ");
        }

        return stringBuilder.toString();
    }

    public static boolean isValidChar(char c) {
        return Charset.forName("US-ASCII")
                .newEncoder()
                .canEncode(c);
    }

}
