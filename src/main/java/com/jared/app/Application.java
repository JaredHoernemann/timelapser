package com.jared.app;

import com.jared.FileService;

import java.io.File;
import java.util.List;

public class Application {
    
    public static void run() {
        List<File> files = FileService.getAllFilesInDirectory("src/main/resources");
        
        System.out.println(files);
        List<File> copies = FileService.copyFiles(files);
        TimestampUtil.timestampImages(copies);
    }
}
