package com.jared;

public class Utils {

    private Utils() {
    }

    ;

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