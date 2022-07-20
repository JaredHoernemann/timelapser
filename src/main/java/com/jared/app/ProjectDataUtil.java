package com.jared.app;

import com.jared.util.FileUtils;

import java.io.File;

public class ProjectDataUtil {

    private static final String METADATA_TXT = "metadata.txt";

    public static boolean hasProjectData(String dir) {
        if (FileUtils.isDirectoryEmpty(dir)) {
            return false;
        } else {
            for (File file : FileUtils.getAllFilesInDirectory(dir)) {
                if (file.getName().equalsIgnoreCase(METADATA_TXT)) {
                    return true;
                }
            }
        }
        return false;
    }
}
