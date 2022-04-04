package com.jared.app;

import com.jared.util.FileService;
import com.jared.util.ImageUtil;

import java.io.File;
import java.util.concurrent.Callable;

public class TimestampTask implements Callable<File> {
    private final File file;

    public TimestampTask(File file) {
        this.file = file;
    }

    @Override
    public File call() throws Exception {
        return ImageUtil.timestampImage(file);
    }
}
