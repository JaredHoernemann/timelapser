package com.jared.utils;

import com.jared.app.Config;

import java.io.File;
import java.util.List;

public class TimelapseUtils {

    private static final String MP4_OUT_DIR = Config.getTimelapseOutputDirectory();
    private static final int FRAME_RATE = Config.getTimelapseFrameRate();
    private static final String FORMAT = "%09d";

    private TimelapseUtils() {
    }

    public static File createTimelapse(List<File> files) {
        System.out.println("Timelapsing " + files.size() + " files");
        long startMillis = System.currentTimeMillis();

        FileUtils.ensureDirectoryExists(MP4_OUT_DIR);
        FileUtils.deleteAllFilesInDirectory(MP4_OUT_DIR);
        String filePath = MP4_OUT_DIR + Config.getTimelapseFileName() + ".mp4";

        String dir = files.get(0).getParent();

        String command = "ffmpeg -framerate " + FRAME_RATE + " -i \"" + dir + "\\frame-" + FORMAT + ".JPG\" " +
                "-s:v " + getResolution() + " -c:v libx264 -crf 17 -pix_fmt yuv420p " + filePath;
        CommandUtils.executeCommandLogToFile(command, "ffmpeg.txt");
        File file = new File(filePath);

        String duration = Utils.millisToPrettyDuration(System.currentTimeMillis() - startMillis);
        System.out.println("ffmpeg command took " + duration);
        System.out.println("Created timelapse: " + file.getAbsolutePath());
        return file;
    }

    private static String getResolution() {
        return Config.getTimelapseResolutionWidth() + "x" + Config.getTimelapseResolutionHeight();
    }
}
