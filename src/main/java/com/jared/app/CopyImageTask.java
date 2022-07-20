package com.jared.app;

import com.jared.util.FileUtils;
import com.jared.util.ImageUtils;

import java.io.File;
import java.util.concurrent.Callable;

public class CopyImageTask implements Callable<File> {

    private final File file;
    private final String toDir;
    private final String toName;
    
    public CopyImageTask(File file, String toDir, String toName) {
        this.file = file;
        this.toDir = toDir;
        this.toName = toName;
        FileUtils.ensureDirectoryExists(toDir);
    }

    @Override
    public File call() throws Exception{
            File f = FileUtils.copyFile(file, toDir, toName);
            if (!Config.cropImages()) {
                return f;
            } else {
                return ImageUtils.cropImage(f);
            }
    }
}
