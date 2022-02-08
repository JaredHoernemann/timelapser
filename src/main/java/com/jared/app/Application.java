package com.jared.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jared.FileService;
import com.jared.Utils;
import com.jared.WebcamUtil;
import com.jared.gson.MetadataGson;

import java.io.File;

@SuppressWarnings("ALL")
public class Application {

    private static final String BASE_OUTPUT_DIR = System.getProperty("user.home") + "\\Timelapse\\";
    private static final int INTERVAL_MINUTES = 4;
    private static final String METADATA_TXT = "metadata.txt";
    private static MetadataGson metadata = null;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public static void main(String[] args) {
        initialize("Apple Fritter");
        loop();
    }

    private static void loop() {
        while (true) {
            
            try {
                File pic = WebcamUtil.takePicture();
                File stamped = TimestampWriter.timestampImage(pic);
                FileService.moveFile(stamped.getPath(), metadata.getProjectDirectory() + stamped.getName());
            } catch (IllegalStateException e) {
                e.printStackTrace();
                System.err.println("Error: Failed to capture image");
            }
            Utils.sleepForMinutes(metadata.getIntervalMinutes());
        }
    }
    
    private static void initialize(String projectName) {
        String dir = BASE_OUTPUT_DIR + projectName + "\\";
        FileService.ensureDirectoryExists(dir);
        String filePath = dir + METADATA_TXT;
        
        if (!MetadataUtil.hasMetadata(dir)) {
            MetadataGson meta = new MetadataGson();
            meta.setProjectName(projectName);
            meta.setIntervalMinutes(INTERVAL_MINUTES);
            meta.setStartTimeMillis(System.currentTimeMillis());
            meta.setProjectDirectory(dir);
            
            String json = gson.toJson(meta);
            FileService.writeToFile(json, filePath);
        }
        
        
        
        String json = FileService.readFileAsString(filePath);
        metadata = gson.fromJson(json, MetadataGson.class);
        System.out.println(json);
    }
}
