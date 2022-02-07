package com.jared;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
}