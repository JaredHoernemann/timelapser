package com.jared.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Utils {

    private Utils() {
    }

    public static String millisToDateStr(long millis, String format) {
        Instant instant = Instant.ofEpochMilli(millis);
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, zoneId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return ldt.format(formatter);
    }

    /**
     * Causes thread to sleep for x number of minutes.
     *
     * @param minutes How long to sleep.
     */
    public static void sleepForMinutes(int minutes) {
        System.out.print("Sleeping for " + minutes + " minutes");
        for (int x = 0; x < minutes * 30; x++) {
            try {
                if (x !=0) {  //skip printing the first dot
                    System.out.print(".");
                }

                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.print("done sleeping");
        System.out.println();
    }


    /**
     * Splits a String containing a delimiter to a List of String values
     *
     * @param string    String object to be split into a list
     * @param delimiter String value method uses to know where to split the initial String input
     * @return A list of trimmed String values
     */
    public static List<String> listFromDelimitedString(String string, String delimiter) {
        List<String> strings;
        strings = (Arrays.asList(string.split(delimiter)));

        //remove whitespace from Strings in list
        int index = 0;
        for (String listItem : strings) {
            listItem = listItem.trim();
            //replace old String with new trimmed String
            strings.set(index, listItem);
            index++;
        }
        return strings;
    }
}