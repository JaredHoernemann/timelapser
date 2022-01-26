package com.jared;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main {

    private static final Dimension RESOLUTION = new Dimension(1920, 1080);
    private static final long START_TIME_MILLIS = System.currentTimeMillis();
    private static final String TIMELAPSE_DIR = System.getProperty("user.home") + "\\Timelapse\\" + millisToDateStr(START_TIME_MILLIS) + "\\";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH mm ss a";


    private static String millisToDateStr(long millis) {
        Instant instant = Instant.ofEpochMilli(millis);
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, zoneId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return ldt.format(formatter);
    }

    public static void main(String[] args) {
        FileService.ensureDirectoryExists(TIMELAPSE_DIR);

        Webcam webcam = Webcam.getDefault();

        webcam.setCustomViewSizes(RESOLUTION);
        webcam.setViewSize(new Dimension(RESOLUTION));
        webcam.open();
        boolean run = true;
        while (run) {

            try {
                String imgName = System.currentTimeMillis() + ".png";
                String filePath = TIMELAPSE_DIR + imgName;
                ImageIO.write(webcam.getImage(), ImageUtils.FORMAT_PNG, new File(filePath));
                System.out.println("Captured image: " + filePath);
                Thread.sleep(30000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                run = false;
            }
        }
    }
}
