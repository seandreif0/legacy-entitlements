package com.meridian.ent.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateUtil {

    private DateUtil() {
    }

    private static final DateTimeFormatter FEED_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String feedStamp(LocalDate d) {
        return FEED_FMT.format(d);
    }
}
