package com.jared.util;

import com.jared.app.CopyFileTask;
import com.jared.app.TimestampTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimelapseUtil {

    private static final String OUTPUT_DIR = "target/-timelapse-mp4/";
    private static final String FORMAT = "%09d";
    private static final int FRAME_RATE = 30;

    private TimelapseUtil() {
    }

    public static File createTimelapse(List<File> files, String projectName) {
        File[] array = new File[files.size()];
        files.toArray(array);
        return createTimelapse(array, projectName);
    }

    public static File createTimelapse(File[] files, String projectName) {
        long startMillis = System.currentTimeMillis();
        FileService.ensureDirectoryExists(OUTPUT_DIR);
        Arrays.sort(files, Comparator.comparingLong(File::lastModified)); //sort by last modified

        String tempDir;
        try {
            tempDir = Files.createTempDirectory("timelapse").toFile().getAbsolutePath() + "/";
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }

        /*
        rename files to FFMPEG compatible format
         */
        int count = 1;
        for (File f : files) {
            String name = "image-" + String.format(FORMAT, count) + ".png";

            Path source = Paths.get(f.getPath());
            try {
                Files.move(source, source.resolveSibling(name));
            } catch (IOException e) {
                throw new IllegalStateException("Failed to rename file: " + f.getAbsolutePath());
            }
            count++;
        }

        List<CopyFileTask> copyFileTasks = new ArrayList<>();
        for (File f : files) {
            CopyFileTask task = new CopyFileTask(f, tempDir);
            copyFileTasks.add(task);
        }
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try {
            service.invokeAll(copyFileTasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<File> copiedFiles = FileService.getAllFilesInDirectory(tempDir);
        List<TimestampTask> timestampTasks = new ArrayList<>();
        for (File f : copiedFiles) {
            TimestampTask task = new TimestampTask(f);
            timestampTasks.add(task);
        }

        try {
            service.invokeAll(timestampTasks);
            service.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FileService.deleteAllFilesInDirectory(OUTPUT_DIR);
        String filePath = OUTPUT_DIR + projectName + ".mp4";

        String command = "ffmpeg -framerate " + FRAME_RATE + " -i \"" + tempDir + "image-" + FORMAT + ".png\" " +
                "-s:v 1920x1080 -c:v libx264 -crf 17 -pix_fmt yuv420p " + filePath;
        CommandUtils.executeCommandLogToFile(command, "ffmpeg.txt");
        File file = new File(filePath);

        String duration = millisToPrettyDuration(System.currentTimeMillis() - startMillis);
        System.out.println("Created timelapse: " + file.getAbsolutePath());
        System.out.println("Timelapse creation took " + duration);
        return file;
    }

    private static String millisToPrettyDuration(long millis) {

        // formula for conversion for
        // milliseconds to minutes.
        long minutes = (millis / 1000) / 60;

        // formula for conversion for
        // milliseconds to seconds
        long seconds = (millis / 1000) % 60;

        StringBuilder stringBuilder = new StringBuilder();
        if (minutes > 0) {
            stringBuilder.append(minutes).append(" minutes and ");
        }
        stringBuilder.append(seconds).append(" seconds");

        // Print the output
        return stringBuilder.toString();
    }
}
