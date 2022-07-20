package com.jared.app;

import com.jared.util.ImageUtils;

import java.io.File;
import java.util.concurrent.Callable;

public class WriteToImageTask implements Callable<File> {
    private final File file;
    private final String text;

    public WriteToImageTask(File file, String text) {
        this.text = text;
        this.file = file;
    }

    @Override
    public File call() {
        return ImageUtils.writeTextToImage(file, text);
    }
}
