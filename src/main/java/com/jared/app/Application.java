package com.jared.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jared.gson.ProjectDataGson;
import com.jared.util.FFMpegUtil;
import com.jared.util.FileService;
import com.jared.util.Utils;
import com.jared.util.WebcamUtils;

import java.io.File;
import java.util.List;

@SuppressWarnings("ALL")
public class Application {

    private static final String BASE_OUTPUT_DIR = System.getProperty("user.home") + "\\Timelapse\\";
    private static final int TAKE_PIC_INTERVAL_MINS = 4;
    private static final long AF_SPROUT_MILLIS = 1642737276840L;
    private static final String APPLE_FRITTER = "Apple Fritter";
    private static final String METADATA_TXT = "metadata.txt";
    private static ProjectDataGson projectData = null;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static File createTimelapse() {
        initialize(APPLE_FRITTER);
        List<File> files = FileService.getAllFilesInDirectory(projectData.getProjectDirectory());
        File[] array = new File[files.size()];
        files.toArray(array);
        return   FFMpegUtil.createTimelapse(array, projectData.getProjectName());
    }
    

    public static void main(String[] args) {
        initialize(APPLE_FRITTER);
        loop();
    }

    private static void loop() {
        while (true) {
            try {
                File file = WebcamUtils.takePicture(projectData.getProjectDirectory());
            } catch (IllegalStateException e) {
                System.err.println("Error: Failed to take picture -> " + e.getMessage());
                e.printStackTrace();
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
            meta.setSproutDateMillis(AF_SPROUT_MILLIS);

            String json = gson.toJson(meta);
            FileService.writeToFile(json, filePath);
        }

        String json = FileService.readFileAsString(filePath);
        projectData = gson.fromJson(json, ProjectDataGson.class);
        System.out.println(json);
    }


    public static int countPngFiles(String dir) {
        int count = 0;
        for (File f : FileService.getAllFilesInDirectory(dir)) {
            boolean isPng = false;
            try {
                String subStr = f.getName().substring(f.getName().length() - 4);
                isPng = subStr.equalsIgnoreCase(".png");
            } catch (IndexOutOfBoundsException e) {
                isPng = false;
            }
            if (isPng) {
                count++;
            }
        }
        return count;
    }
}
