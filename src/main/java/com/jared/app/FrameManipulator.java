package com.jared.app;

import com.jared.utils.FileUtils;
import com.jared.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FrameManipulator {

    private static final String FORMAT = "%09d";
    private static final String PREVIEW_DIR = "target/frame-preview/";
    private List<File> frames;
    private final long firstFrameMillis;

    private FrameManipulator(List<File> originalFiles) {
        List<File> pictures = FileUtils.filterOnlyPictures(originalFiles);

        if (pictures.isEmpty()) {
            throw new IllegalStateException("List of pictures cannot be empty");
        }

        this.frames = FileUtils.sortFilesByLastModified(pictures);
        this.firstFrameMillis = FileUtils.getLastModifiedMillis(this.frames.get(0));
        setNumHoursToTimelapse(Config.getTimelapseNumHours());
        System.out.println("Manipulating " + frames.size() + " frames");
    }

    private void setNumHoursToTimelapse(Integer numHours) {
        if (!Objects.isNull(numHours)) {
            frames = FileUtils.getMostRecentFiles(frames, numHours);
        }
    }

    public static FrameManipulator create(String filesDir) {
        List<File> listOfFiles = FileUtils.getAllFilesInDirectory(filesDir);
        return new FrameManipulator(listOfFiles);
    }

    public File createPreview() {
        String dir = PREVIEW_DIR;
        FileUtils.ensureDirectoryExists(dir);
        String name = "preview.JPG";
        File mostRecentFile = frames.get(frames.size() -1);
        ManipulateFrameTask task = new ManipulateFrameTask(mostRecentFile, dir, name, firstFrameMillis);

        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            List<ManipulateFrameTask> temp = new ArrayList<>();
            temp.add(task);
            service.invokeAll(temp);
            service.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return FileUtils.getAllFilesInDirectory(dir).get(0);
    }

    public List<File> manipulateFrames() {
        long startMillis = System.currentTimeMillis();

        String tempDir = FileUtils.createTempDirectory("timelapse");
        List<ManipulateFrameTask> frameTasks = new ArrayList<>();
        int count = 1;
        for (File f : frames) {
            String name = "frame-" + String.format(FORMAT, count) + ".JPG";
            ManipulateFrameTask task = new ManipulateFrameTask(f, tempDir, name, firstFrameMillis);
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
