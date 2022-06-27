package com.jared.app;

import com.jared.util.FileService;
import com.jared.util.ImageUtil;

import java.io.File;
import java.util.concurrent.Callable;

public class CopyFileTask implements Callable<File> {

    private final File file;
    private final String toDir;
    private final String toName;
    
    public CopyFileTask(File file, String toDir, String toName) {
        this.file = file;
        this.toDir = toDir;
        this.toName = toName;
        FileService.ensureDirectoryExists(toDir);
    }

    @Override
    public File call() {
        if (ImageUtil.isLightOn(file)) {
            return FileService.copyFile(file, toDir, toName);
        } else {
            System.out.println("Skipping file copy -> " + file.getName() + " (lights are off)");
            return null;
        }
    }
}
