package com.example.sophistnerd.util;

import java.text.SimpleDateFormat;

public class TimeHelper {

    public static String getFormatTime(String timeFormat, long currentTime) {
        if (timeFormat == null || timeFormat.length() < 1) {
            timeFormat = "MM-dd-HH-mm-ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        return simpleDateFormat.format(currentTime);
    }

    public static String getFormatTime() {
        return getFormatTime("MM-dd-HH-mm-ss", System.currentTimeMillis());
    }
}
