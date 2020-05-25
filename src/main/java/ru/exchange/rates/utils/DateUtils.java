package ru.exchange.rates.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";


    public static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date date = new Date();
        return formatter.format(date);
    }

}
