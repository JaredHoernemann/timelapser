package com.jared.app;

import com.jared.utils.FileUtils;
import com.jared.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FrameManipulator {

    private static final String FORMAT = "%09d";
    private final List<File> originalFrames;
    private final long firstFrameMillis;

    private FrameManipulator(List<File> originalFiles) {
        List<File> pictures = FileUtils.filterOnlyPictures(originalFiles);

        if (pictures.isEmpty()) {
            throw new IllegalStateException("List of pictures cannot be empty");
        }

        this.originalFrames = FileUtils.sortFilesByLastModified(pictures);
        this.firstFrameMillis = FileUtils.getLastModifiedMillis(this.originalFrames.get(0));
    }

    public static FrameManipulator create(String filesDir, int numHours) {
        List<File> listOfFiles = FileUtils.getAllFilesInDirectory(filesDir);
        List<File> mostRecentPics = FileUtils.getMostRecentFiles(listOfFiles, numHours);
        return new FrameManipulator(mostRecentPics);
    }

    public List<File> manipulateFrames() {
        long startMillis = System.currentTimeMillis();

        String tempDir = FileUtils.createTempDirectory("timelapse");
        List<ModifyFrameTask> frameTasks = new ArrayList<>();
        int count = 1;
        for (File f : originalFrames) {
            String name = "frame-" + String.format(FORMAT, count) + ".JPG";
            ModifyFrameTask task = new ModifyFrameTask(f, tempDir, name, firstFrameMillis);
            frameTasks.add(task);
            count++;
        }

        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            service.invokeAll(frameTasks);
            service.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String duration = Utils.millisToPrettyDuration(System.currentTimeMillis() - startMillis);
        System.out.println("Frame manipulation took " + duration);

        return FileUtils.getAllFilesInDirectory(tempDir);
    }
}
