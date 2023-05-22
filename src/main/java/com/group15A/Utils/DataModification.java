package com.group15A.Utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * A class storing methods related to changing given data to a different format.
 *
 * @author Filip Fois
 */
public class DataModification
{
    /**
     * Capitalizes string
     *
     * @return Capitalized version of given string (first letter uppercase and rest lowercase)
     */
    public static String capitalize(String string)
    {
        String newString = "";
        if(string != null && string.length() > 0) {
            newString += string.substring(0,1).toUpperCase();
            newString += string.substring(1).toLowerCase();
        }
        return newString;
    }

    /**
     * Receive a timestamp and return a shortened string version in format:
     * "dayName, dayNumber monthName, yearNumber, hour:minute"
     *
     * @param timestamp The timestamp to be shortened
     * @return shortened timestamp string
     */
    public static String fullDate(Timestamp timestamp)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM, yyyy");
        return dateFormat.format(timestamp);
    }

    /**
     * Converts a timestamp to HH:mm format
     *
     * @param timestamp
     * @return a string (hh:mm) from a timestamp (yyyy-mm-dd hh:mm:ss)
     */
    public static String getTime(Timestamp timestamp)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(timestamp);
    }

    /**
     * Receive a timestamp and return a shortened string version in format:
     * "dayNumber/monthNumber/yearNumber, hour:minute"
     *
     * @param timestamp The timestamp to be shortened
     * @return shortened timestamp string
     */
    public static String shortDateTime(Timestamp timestamp)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd/MM/yyyy, HH:mm");
        return dateFormat.format(timestamp);
    }


}
