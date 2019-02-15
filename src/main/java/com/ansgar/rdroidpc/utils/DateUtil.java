package com.ansgar.rdroidpc.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static final String DATE_FORMAT = "yyyyMMddHHmmss";

    public static String getCurrentDate() {
        return getCurrentDate(DATE_FORMAT);
    }

    public static String getCurrentDate(String formate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formate);
        return dateFormat.format(new Date());
    }

}
