package com.jared;

import com.jared.app.Application;
import com.jared.app.TimestampUtil;

import java.awt.*;
import java.io.File;

//public class Main extends Application {
public class Main {

    private static final Dimension RESOLUTION = new Dimension(1920, 1080);
    private static final long START_TIME_MILLIS = System.currentTimeMillis();
    private static final String DATE_FORMAT = "yyyy-MM-dd HHmm a ss";
    private static final String TIMELAPSE_DIR = System.getProperty("user.home") + "\\Timelapse\\" + Utils.millisToDateStr(START_TIME_MILLIS, DATE_FORMAT) + "\\";
    private static final int TIMELAPSE_INTERVAL_MINUTES = 2;


    //    @Override
//    public void start(Stage primaryStage)  {
//        try {
//            FXMLLoader loader = new FXMLLoader(
//                    Main.class.getResource("/SearchController.fxml"));
//            AnchorPane page = (AnchorPane) loader.load();
//            Scene scene = new Scene(page);
//
//            primaryStage.setTitle("Title");
//            primaryStage.setScene(scene);
//            primaryStage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
    public static void main(String[] args) {
        Application.run();
        
        
//        File image = new File("src/main/resources/20220205_165320(0).jpg");
//        
//        File copy = FileService.copyFile(image);
//        
//        
//        TimestampUtil.timestampImage(copy, "MMMM dd: h:mma");
//        
//        File img2 = new File("src/main/resources/test-capture-1.png");
//        long desc = TimestampUtil.getLastModifiedMillis(image);
//        long desc2 = TimestampUtil.getLastModifiedMillis(img2);
//
//        System.out.println(desc);
//        System.out.println();
//        System.out.println(desc2);
        
//        
//        for (Directory directory : metadata.getDirectories()) {
//            for (Tag tag : directory.getTags()) {
//                System.out.println("Tag type: " + tag.getTagType() + " -> " + tag);
//                System.out.println("Tag name: " + tag.getTagName() + " -> " + tag);
//                System.out.println("Tag description: " + tag.getDescription());
//                
//            }
//
//            for (String error : directory.getErrors()) {
//                System.err.println("Error: " + error);
//            }
        
        
       
        
        
        
        
        
        
        
        
        
//        launch(args);
        
        
//        FileService.ensureDirectoryExists(TIMELAPSE_DIR);
//
//        Webcam webcam = Webcam.getDefault();
//
//        webcam.setCustomViewSizes(RESOLUTION);
//        webcam.setViewSize(new Dimension(RESOLUTION));
//        webcam.open();
//        boolean run = true;
//        while (run) {
//
//            try {
//                String imgName = millisToDateStr(System.currentTimeMillis(), DATE_FORMAT) + ".png";
//                String filePath = TIMELAPSE_DIR + imgName;
//                ImageIO.write(webcam.getImage(), ImageUtils.FORMAT_PNG, new File(filePath));
//                System.out.println("Captured image: " + filePath);
//                Utils.sleepForMinutes(TIMELAPSE_INTERVAL_MINUTES);
//            } catch (IOException e) {
//                e.printStackTrace();
//                run = false;
//            }
//        }
    }
}
