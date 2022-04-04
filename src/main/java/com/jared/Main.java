package com.jared;

import com.jared.app.Application;
import com.jared.camera.CameraService;
import com.jared.util.FileService;
import com.jared.util.LoggerService;
import com.jared.util.TimelapseUtil;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

public class Main {

    private static final Logger log = LoggerService.getLoggerForClass();


    public static void main(String[] args) throws Exception {

        Application.createTimelapse();
        Application.main(args);
//        List<File> list = FileService.getAllFilesInDirectory("C:\\Users\\jared\\Desktop\\Pics");
//        TimelapseUtil.createTimelapse(list, "test-lapse");
    }



    public static void init() throws Exception {
        
    }
}

