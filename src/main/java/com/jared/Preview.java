package com.jared;

import com.jared.app.Config;
import com.jared.app.FrameManipulator;

public class Preview {

    public static void main(String[] args) {
        FrameManipulator frameManipulator = FrameManipulator.create(Config.getTimelapsePicturesDirectory());
        frameManipulator.createPreview();
    }
}
