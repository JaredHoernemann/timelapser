package com.jared.util;

import com.jared.app.Config;
import com.jared.app.CopyImageTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimelapseUtil {

    private static final String MP4_OUT_DIR = Config.getTimelapseOutputDirectory();
    private static final int FRAME_RATE = Config.getTimelapseFrameRate();
    private static final String RESOLUTION = Config.getTimelapseResolution();
    private static final String FORMAT = "%09d";

    private TimelapseUtil() {
    }


    public static File createTimelapse(List<File> files) {
        System.out.println("Timelapsing " + files.size() + " files");
        long startMillis = System.currentTimeMillis();
        FileUtils.ensureDirectoryExists(MP4_OUT_DIR);

        String tempDir = FileUtils.createTempDirectory("timelapse");
        files = FileUtils.sortFilesByLastModified(files);

        List<CopyImageTask> copyFileTasks = new ArrayList<>();
        int count = 1;
        for (File f : files) {
            String name = "image-" + String.format(FORMAT, count) + ".JPG";
            CopyImageTask task = new CopyImageTask(f, tempDir, name);
            copyFileTasks.add(task);
            count++;
        }
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try {
            service.invokeAll(copyFileTasks);
            service.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        List<File> copiedFiles = FileService.getAllFilesInDirectory(tempDir);
//        List<TimestampAndCropTask> timestampTasks = new ArrayList<>();
//        for (File f : copiedFiles) {
//            TimestampAndCropTask task = new TimestampAndCropTask(f);
//            timestampTasks.add(task);
//        }
//
//        service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//
//        try {
//            service.invokeAll(timestampTasks);
//            service.shutdown();
//        } catch (InterruptedException e) {
//            System.err.println("Error");
//            e.printStackTrace();
//        }

        FileUtils.deleteAllFilesInDirectory(MP4_OUT_DIR);
        String filePath = MP4_OUT_DIR + "rename_this" + ".mp4";

        String command = "ffmpeg -framerate " + FRAME_RATE + " -i \"" + tempDir + "image-" + FORMAT + ".JPG\" " +
                "-s:v " + RESOLUTION + " -c:v libx264 -crf 17 -pix_fmt yuv420p " + filePath;
        CommandUtils.executeCommandLogToFile(command, "ffmpeg.txt");
        File file = new File(filePath);

        String duration = millisToPrettyDuration(System.currentTimeMillis() - startMillis);
        System.out.println("Created timelapse: " + file.getAbsolutePath());
        System.out.println("Timelapse generation took " + duration);
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
