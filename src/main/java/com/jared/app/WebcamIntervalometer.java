package com.jared.app;

import com.jared.utils.ImageUtils;
import com.jared.utils.Utils;
import com.jared.camera.WebcamCamera;

import java.io.File;

@SuppressWarnings("ALL")
public class WebcamIntervalometer {

    private static final String BASE_OUTPUT_DIR = System.getProperty("user.home") + "\\Timelapse\\";
    private static final int TAKE_PIC_INTERVAL_MINS = Config.getWebcamPictureIntervalMinutes();

    public static void main(String[] args) {
        loop();
    }

    private static void loop() {
        while (true) {
            try {
                File file = WebcamCamera.takePicture();
                if (!ImageUtils.isLightOn(file)) {
                    System.out.println("Lights are off, deleting file: " + file.getName());
                    file.delete();

                }
            } catch (IllegalStateException e) {
                System.err.println("Error: Failed to take picture -> " + e.getMessage());
                e.printStackTrace();
            }
            Utils.sleepForMinutes(TAKE_PIC_INTERVAL_MINS);
        }
    }
}
