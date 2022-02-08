package com.jared.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jared.util.FileService;
import com.jared.util.Utils;
import com.jared.util.WebcamUtil;
import com.jared.gson.ProjectDataGson;

import java.io.File;

@SuppressWarnings("ALL")
public class Application {

    private static final String BASE_OUTPUT_DIR = System.getProperty("user.home") + "\\Timelapse\\";
    private static final int TAKE_PIC_INTERVAL_MINS = 4;
    private static final String METADATA_TXT = "metadata.txt";
    private static ProjectDataGson projectData = null;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public static void main(String[] args) {
        initialize("Apple Fritter");
        loop();
    }

    private static void loop() {
        while (true) {
            
            try {
                File file = WebcamUtil.takePicture();
                
                File stamped = TimestampWriter.timestampImage(file);
                FileService.moveFile(stamped.getPath(), projectData.getProjectDirectory() + stamped.getName());
            } catch (IllegalStateException e) {
                e.printStackTrace();
                System.err.println("Error: Failed to capture image");
            }
            Utils.sleepForMinutes(projectData.getIntervalMinutes());
        }
    }
    
    private static void initialize(String projectName) {
        String dir = BASE_OUTPUT_DIR + projectName + "\\";
        FileService.ensureDirectoryExists(dir);
        String filePath = dir + METADATA_TXT;
        
        if (!ProjectDataUtil.hasProjectData(dir)) {
            ProjectDataGson meta = new ProjectDataGson();
            meta.setProjectName(projectName);
            meta.setIntervalMinutes(TAKE_PIC_INTERVAL_MINS);
            meta.setStartTimeMillis(System.currentTimeMillis());
            meta.setProjectDirectory(dir);
            meta.setSproutDateMillis(1642737276840L);
            
            String json = gson.toJson(meta);
            FileService.writeToFile(json, filePath);
        }
        
        
        
        String json = FileService.readFileAsString(filePath);
        projectData = gson.fromJson(json, ProjectDataGson.class);
        System.out.println(json);
    }
}
