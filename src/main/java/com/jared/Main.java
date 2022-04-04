package com.jared;

import com.jared.app.Application;

public class Main {

    public static void main(String[] args) {
        Application.compileTimelapse();
        Application.main(args);

//        List<File> list = FileService.getAllFilesInDirectory("C:\\Users\\jared\\Desktop\\Pics");
//        File[] files = new File[list.size()];
//
//        File f = FFMpegUtil.createTimelapse(list.toArray(files), "timelapse");

//        Application.main(args);
//        FileService.ensureDirectoryExists("C:\\Users\\jared\\Desktop\\Pics2\\");
//        FileService.ensureDirectoryExists("target/timelapse-mp4/");
//        FileService.deleteAllFilesInDirectory("target/timelapse-mp4/");
//        FileService.deleteAllFilesInDirectory("C:\\Users\\jared\\Desktop\\Pics2\\");
//        List<File> files = FileService.getAllFilesInDirectory("C:\\Users\\jared\\Desktop\\Pics");
//
//        int count = 1;
//        for (File f : files) {
//            String name = "image" + String.format("%010d", count) + ".png";
//
//            File copy = FileService.copyFile(f);
//            FileService.moveFile(copy.getPath(), "C:\\Users\\jared\\Desktop\\Pics2\\" + name);
//            count++;
//        }
//        
//        String command = "ffmpeg -framerate 15 -i \"C:/Users/jared/Desktop/Pics2/image%05d.png\" " +
//                "-s:v 1920x1080 -c:v libx264 -crf 17 -pix_fmt yuv420p target/timelapse-mp4/apple-fritter.mp4";
//
//        CommandUtils.executeCommandLogToFile(command, "ffmpeg.txt");

    }

    private static int getRed(int pixel) {
        return (pixel >> 16) & 0xFF;
    }

    private static int getGreen(int pixel) {
        return (pixel >> 8) & 0xFF;
    }

    private static int getBlue(int pixel) {
        return (pixel) & 0xFF;
    }
}

