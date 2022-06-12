package com.example.plugin_module.helper;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeFormat {

    private static String TIME_FORMAT_STRING = "dd-HH-ss";
    private static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(TIME_FORMAT_STRING, Locale.CHINA);

    public static String getCurrentTime() {
        return SIMPLE_DATE_FORMAT.format(System.currentTimeMillis());
    }
}
