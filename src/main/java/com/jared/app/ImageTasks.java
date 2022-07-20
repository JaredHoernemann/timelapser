package com.jared.app;

import com.jared.util.FileUtils;
import com.jared.util.ImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageTasks {

    private static final String FORMAT = "%09d";
    private static final String TEMP_DIR_PREFIX = "timelapse";
    private static final String IMG_FILE_EXT = ".JPG";

    private ImageTasks() {
    }

    public static List<File> timestampImages(List<File> images) {
        File first = FileUtils.sortFilesByLastModified(images).get(0);
        long startMillis = first.lastModified();

        List<WriteToImageTask> tasks = new ArrayList<>();

        for (File i : images) {
            String text ="Hour: " + ImageUtils.calcHoursBetweenMillis(i.lastModified(), startMillis);
            WriteToImageTask t = new WriteToImageTask(i, text);
            tasks.add(t);
        }

        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            service.invokeAll(tasks);
            service.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }




    public static List<File> copyImages(List<File> files) {
        files = FileUtils.filterOnlyPictures(files);
        String tempDir = FileUtils.createTempDirectory(TEMP_DIR_PREFIX);
        List<CopyImageTask> imagesTasks = new ArrayList<>();
        int count = 1;
        for (File f : FileUtils.sortFilesByLastModified(files)) {
            String name = "image-" + String.format(FORMAT, count) + IMG_FILE_EXT;
            CopyImageTask task = new CopyImageTask(f, tempDir, name);
            imagesTasks.add(task);
            count++;
        }

        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            service.invokeAll(imagesTasks);
            service.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(tempDir);
        return FileUtils.getAllFilesInDirectory(tempDir);
    }


}
