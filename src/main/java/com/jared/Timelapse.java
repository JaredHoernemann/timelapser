package com.jared;

import com.jared.app.Config;
import com.jared.app.FrameManipulator;
import com.jared.utils.TimelapseUtils;

import java.io.File;
import java.util.List;

public class Timelapse {

    public static void main(String[] args) {
        FrameManipulator frameManipulator = FrameManipulator.create(Config.getTimelapsePicturesDirectory());
        List<File> files = frameManipulator.manipulateFrames();
        TimelapseUtils.createTimelapse(files);
    }
}

