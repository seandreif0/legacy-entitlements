package com.meridian.ent.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil {

    private DateUtil() {
    }

    // thread safe
    private static final SimpleDateFormat FEED_FMT = new SimpleDateFormat("yyyyMMdd");

    public static String feedStamp(Date d) {
        return FEED_FMT.format(d);
    }

    public static Date minusDays(Date d, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DAY_OF_MONTH, -days);
        return c.getTime();
    }
}
