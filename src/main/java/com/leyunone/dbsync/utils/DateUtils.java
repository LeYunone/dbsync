package com.leyunone.dbsync.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author leyunone
 * @date 2021/11/4
 */
public class DateUtils {

    public static String getBeforeDaysDateTime(int day) {
        Date date = null;
        Calendar cd = Calendar.getInstance();
        cd.add(Calendar.DATE, -day);
        cd.set(Calendar.HOUR_OF_DAY, 0);
        cd.set(Calendar.MINUTE, 0);
        cd.set(Calendar.SECOND, 0);
        cd.set(Calendar.MILLISECOND, 0);
        date = cd.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String str = df.format(date);
        return str;
    }

}
