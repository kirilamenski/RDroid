package com.ansgar.rdroidpc.utils;

import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.entities.Option;

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
        Option option = device.getOption();
        int _bitRate = option != null ? option.getBitRate() : bitRate;
        int height = option != null ? option.getHeight() : device.getHeight();
        int width = option != null ? option.getWidth() : device.getWidth();
        String command = String.format(
                Locale.ENGLISH,
                AdbCommandEnum.SCREEN_RECORD.getCommand(),
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


    public static String getEncodedString(char ch) {
        byte[] bytes = String.valueOf(ch).getBytes();
        return new String(bytes, Charset.forName("UTF-8"));
    }

}
