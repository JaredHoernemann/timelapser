package com.jared;

import com.jared.app.Config;
import com.jared.app.ImageTasks;
import com.jared.object.TimelapseObject;
import com.jared.util.FileUtils;
import com.jared.util.TimelapseUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<File> dirOne = FileUtils.getAllFilesInDirectory(Config.getTimelapsePicturesDirectory());
        List<File> files = FileUtils.getMostRecentFiles(FileUtils.filterOnlyPictures(dirOne), Config.getTimelapseNumHours());
        TimelapseUtil.createTimelapse(files);
    }
}

