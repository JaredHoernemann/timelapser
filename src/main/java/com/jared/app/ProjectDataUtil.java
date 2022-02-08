package com.jared.app;

import com.jared.util.FileService;

import java.io.File;

public class ProjectDataUtil {

    private static final String METADATA_TXT = "metadata.txt";

    public static boolean hasProjectData(String dir) {
        if (FileService.isDirectoryEmpty(dir)) {
            return false;
        } else {
            for (File file : FileService.getAllFilesInDirectory(dir)) {
                if (file.getName().equalsIgnoreCase(METADATA_TXT)) {
                    return true;
                }
            }
        }
        return false;
    }
}
