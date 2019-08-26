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
        String command = String.format(
                Locale.ENGLISH,
                AdbCommandEnum.SCREEN_STREAMING.getCommand(),
                option.getBitRate(),
                option.getHeight(),
                option.getWidth()
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
