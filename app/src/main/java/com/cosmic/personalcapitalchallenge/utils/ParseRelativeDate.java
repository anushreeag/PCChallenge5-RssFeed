package com.cosmic.personalcapitalchallenge.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by anushree on 10/19/2017.
 * Util class to get the required format to
 * display from the format which is present in RSS Feed
 */

public class ParseRelativeDate {


    public static String getExpandedDate(String rawRssDate) {
        String personalCapitalFormat = "EEE, dd MMM yyyy HH:mm:ss ZZZZZ";
        SimpleDateFormat sf = new SimpleDateFormat(personalCapitalFormat, Locale.ENGLISH);
        Calendar cal = Calendar.getInstance();
        try {
            Date d = sf.parse(rawRssDate);
            cal.setTime(d); // Set up a calendar instance so we can retrive the day, Month and year
            return new SimpleDateFormat("MMMM dd, yyyy").format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;


    }


}
