package com.jared.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;

public class FFMpegUtil {

    private static final String OUTPUT_DIR = "target/mp4/";
    private static final String FORMAT = "%09d";
    private static final int FRAME_RATE = 90;

    private FFMpegUtil() {
    }

    public static File createTimelapse(File[] files, String projectName) {
        FileService.ensureDirectoryExists(OUTPUT_DIR);
        Arrays.sort(files, Comparator.comparingLong(File::lastModified));

        String tempDir;
        try {
            tempDir = Files.createTempDirectory("timelapse").toFile().getAbsolutePath() + "/";
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }

        int count = 1;
        for (File f : files) {
            String name = "image-" + String.format(FORMAT, count) + ".png";
            File copy = FileService.copyFile(f, tempDir, name);
            ImageUtil.timestampImage(copy, ImageUtil.DEFAULT_FORMAT);
            count++;
        }
        FileService.deleteAllFilesInDirectory(OUTPUT_DIR);
        String filePath = OUTPUT_DIR + projectName + ".mp4";

        String command = "ffmpeg -framerate " + FRAME_RATE + " -i \"" + tempDir + "image-" + FORMAT + ".png\" " +
                "-s:v 1920x1080 -c:v libx264 -crf 17 -pix_fmt yuv420p " + filePath;
        CommandUtils.executeCommandLogToFile(command, "ffmpeg.txt");
        File file = new File(filePath);
        System.out.println("Created timelapse: " + file.getAbsolutePath());
        return file;
    }
}
