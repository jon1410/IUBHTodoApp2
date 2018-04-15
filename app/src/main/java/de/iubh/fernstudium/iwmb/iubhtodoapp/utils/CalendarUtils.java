package de.iubh.fernstudium.iwmb.iubhtodoapp.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {

    private static final String DEFAULT_DATEFORMAT = "dd.MM.yyyy";

    public static Date fromStringToDate(String date){
     return fromStringToDate(date, DEFAULT_DATEFORMAT);
    }

    public static Calendar fromStringToCalendar(String date){
        Calendar cal = Calendar.getInstance();
        Date d = fromStringToDate(date, DEFAULT_DATEFORMAT);
        if(d != null){
            cal.setTime(d);
            return cal;
        }
        return null;
    }

    public static Date fromStringToDate(String sDate, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date = sdf.parse(sDate);
            return date;
        } catch (ParseException e) {
            Log.e("CalendarUtils", String.format("Could not format given Date %s with given Pattern %s", sDate, pattern));
        }
        return null;
    }
}
