package com.jared.app;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.util.ImageUtils;
import com.jared.FileService;
import com.jared.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static com.jared.Utils.millisToDateStr;

public class WebcamController {

    private static final Dimension RESOLUTION = new Dimension(1920, 1080);
    private static final long START_TIME_MILLIS = System.currentTimeMillis();
    private static final String DATE_FORMAT = "yyyy-MM-dd HHmm a ss";
//    private static final String TIMELAPSE_DIR = System.getProperty("user.home") + "\\Timelapse\\" + Utils.millisToDateStr(START_TIME_MILLIS, DATE_FORMAT) + "\\";
    private static final String TIMELAPSE_DIR = "target/timelapse/";
    private static final int TIMELAPSE_INTERVAL_MINUTES = 2;
    
    public static File takePicture() {
        FileService.ensureDirectoryExists(TIMELAPSE_DIR);

        Webcam webcam = Webcam.getDefault();

        webcam.setCustomViewSizes(RESOLUTION);
        webcam.setViewSize(new Dimension(RESOLUTION));
        webcam.open();

        String filePath;
        try {
            String imgName = millisToDateStr(System.currentTimeMillis(), DATE_FORMAT) + ".png";
            filePath = TIMELAPSE_DIR + imgName;
            ImageIO.write(webcam.getImage(), ImageUtils.FORMAT_PNG, new File(filePath));
            System.out.println("Captured image: " + filePath);
            Utils.sleepForMinutes(TIMELAPSE_INTERVAL_MINUTES);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
        return new File(filePath);
    }
    
}
