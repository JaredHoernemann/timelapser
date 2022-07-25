package com.jared.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jared.camera.WebcamCamera;
import com.jared.utils.ImageUtils;
import com.jared.utils.Utils;

import java.io.File;

@SuppressWarnings("ALL")
public class Application {

    private static final String BASE_OUTPUT_DIR = System.getProperty("user.home") + "\\Timelapse\\";
    private static final int TAKE_PIC_INTERVAL_MINS = 4;
    private static final long AF_SPROUT_MILLIS = 1642737276840L;
    private static final String APPLE_FRITTER = "CBD2";
    private static final String METADATA_TXT = "metadata.txt";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void compileTimelapse(int numDays) {
    }


    public static void main(String[] args) {
        loop();
    }

    private static void loop() {
        while (true) {
            try {
                File file = WebcamCamera.takePicture("C:\\Users\\Jared\\IdeaProjects\\timelapser\\target\\webcam-captures\\");
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
