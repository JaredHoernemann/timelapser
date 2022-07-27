package com.jared.app;

import com.jared.utils.FileUtils;
import com.jared.utils.ImageUtils;
import com.jared.utils.Utils;

import java.io.File;
import java.util.concurrent.Callable;

public class ManipulateFrameTask implements Callable<File> {

    private final File file;
    private final String toDir;
    private final String toName;
    private final long firstFrameMillis;
    private final long frameMillis;

    public ManipulateFrameTask(File file, String toDir, String toName, long firstFrameMillis) {
        this.firstFrameMillis = firstFrameMillis;
        this.frameMillis = FileUtils.getLastModifiedMillis(file);
        this.file = file;
        this.toDir = toDir;
        this.toName = toName;
        FileUtils.ensureDirectoryExists(toDir);
    }

    @Override
    public File call() throws Exception {
        File copy = FileUtils.copyFile(file, toDir, toName);
        if (Config.cropImages()) {
            ImageUtils.cropImage(copy,
                    Config.getCropStartX(), Config.getCropStartY(),
                    Config.getCropWidthNumPixels(), Config.getCropHeightNumPixels());
        }

        if (Config.timestampImages()) {
            int day = Utils.calcDaysBetweenMillis(firstFrameMillis, frameMillis);
            ImageUtils.writeTextToImage(copy, "Day " + (day + 1));
        }
        return copy;
    }
}
