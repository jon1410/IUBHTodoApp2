package de.iubh.fernstudium.iwmb.iubhtodoapp.utils;

import android.util.Log;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {

    private static final String DEFAULT_DATEFORMAT = "dd.MM.yyyy";

    public static Timestamp fromStringToTimestamp(String date){
        Date d = fromStringToDate(date);
        return new Timestamp(d.getTime());
    }

    public static Date fromStringToDate(String date) {
        return fromStringToDate(date, DEFAULT_DATEFORMAT);
    }

    public static Calendar fromStringToCalendar(String date) {
        Calendar cal = Calendar.getInstance();
        Date d = fromStringToDate(date, DEFAULT_DATEFORMAT);
        if (d != null) {
            cal.setTime(d);
            return cal;
        }
        return null;
    }

    public static Date fromStringToDate(String sDate, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date = sdf.parse(sDate);
            return date;
        } catch (ParseException e) {
            Log.e("CalendarUtils", String.format("Could not format given Date %s with given Pattern %s", sDate, pattern));
        }
        return null;
    }

    public static boolean isToday(Date first){

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(first);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}
