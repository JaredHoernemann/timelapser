package com.jared;

import com.jared.app.Config;
import com.jared.app.FrameManipulator;
import com.jared.utils.TimelapseUtils;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        FrameManipulator modifier = FrameManipulator.create(Config.getTimelapsePicturesDirectory(), Config.getTimelapseNumHours());
        List<File> files = modifier.manipulateFrames();
        TimelapseUtils.createTimelapse(files);
    }
}

