package org.barakahchicago.barakah.util;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

public class DateUtil {

    /*
        parses a standard dateTime string and returns a yyyy-MM-dd HH:mm:ss format
     */
    public static LocalDateTime getLocalDateTime(String dateTime) {
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(dateTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            return null;
        }
        return localDateTime;
    }

    /*
           parses a standard dateTime string and returns a EEE, MMM d format
         */
    public static String getFormattedDate(String dateTime) {

        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(dateTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            return "";
        }

        return localDateTime.toString("EEE, MMM d");


    }

    /*
        parses a standard dateTime string and returns a h:mm a format
     */
    public static String getFormattedTime(String dateTime) {
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(dateTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            return "";
        }

        return localDateTime.toString("h:mm a");

    }

    /*
         parses a standard dateTime string and returns EEE, d MMM h:mm a format
     */
    public static String getFormattedDateTime(String dateTime) {
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(dateTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            return "";
        }

        return localDateTime.toString("EEE, d MMM h:mm a");


    }

    /*
         parses a standard dateTime string and returns time in milliseconds
      */
    public static long getTimeInMillis(String dateTime) {
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(dateTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            return 0;
        }

        return localDateTime.toDateTime().getMillis();


    }
}

