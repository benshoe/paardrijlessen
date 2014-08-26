package com.gmail.benshoe.paardrijlessen.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ben on 8/21/14.
 */
public class DateUtil {
    public static String dateFrom(long dateValue) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Long.valueOf(dateValue));
        Date d = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        return sdf.format(d);
    }
}
