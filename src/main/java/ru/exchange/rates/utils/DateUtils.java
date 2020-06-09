package ru.exchange.rates.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String DEFAULT_DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";


    public static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        Date date = new Date();
        return formatter.format(date);
    }

    public static String getCurrentDate(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = new Date();
        return formatter.format(date);
    }


}
