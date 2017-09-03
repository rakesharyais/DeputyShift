package com.deputyshift.Utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateTimeConverter {

    public static Date convertStringToDate(String date) {
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        Date finaldate = null;
        try {
            finaldate = df1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finaldate;

    }

    public static String convertDateToString() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String nowAsISO = df.format(new Date());
        return nowAsISO;
    }


}
