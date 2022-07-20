package com.jared.object;

import com.jared.app.CopyImageTask;
import com.jared.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimelapseObject {

    private final List<File> files;
    private static final String FORMAT = "%09d";
    private static final String TEMP_DIR_PREFIX = "timelapse";
    private static final String IMG_FILE_EXT = ".JPG";


    public TimelapseObject(List<File> files) {
        this.files = FileUtils.filterOnlyPictures(files);
    }

    public List<File> processImages() {
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
