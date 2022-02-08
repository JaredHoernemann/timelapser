package com.jared.app;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.file.FileSystemDirectory;
import com.jared.gson.ProjectDataGson;
import com.jared.util.Utils;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimestampWriter {
    
    public static String DEFAULT_FORMAT = "MMMM dd: h:mma";
    

    
    public static File timestampImage(File file) {
        return timestampImage(file, DEFAULT_FORMAT);
    }
    
    
    public static File timestampImage(File file, String format) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            
            Font font = new Font("Arial", Font.BOLD, 72);
            long millis = getLastModifiedMillis(file);
            String timestamp = Utils.millisToDateStr(millis, format) 
                    + "     (Day " + calcDaysBetweenMillis(millis, 1642737276840L) + ")";
            
            Graphics graphics = bufferedImage.getGraphics();
            graphics.setFont(font);
            graphics.setColor(Color.RED);
            graphics.drawString(timestamp, 150, 150);
            graphics.dispose();
            ImageIO.write(bufferedImage, "png", file);
            System.out.println("Timestamped image: " + file.getName() + " -> " + timestamp);
            return file;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    
    public static int calcDaysBetweenMillis(long first, long second) {
        long elapsed = Math.abs(first - second);
        long millisInDay = 1000 * 60 * 60 * 24;
        
        int count = 0;
        for (long x = elapsed; x>=millisInDay; x-=millisInDay) {
            count++;
        }
        return count;
    }
    
    
    

    public static long getLastModifiedMillis(File image) {
        Metadata metadata = getMetaData(image);
        FileSystemDirectory directory = metadata.getFirstDirectoryOfType(FileSystemDirectory.class); //FileSystemDirectory contains Last Modified metadata
        Date date = directory.getDate(3); //type 3 = Last Modified Date
        return date.getTime();
    }

    private static Metadata getMetaData(File image) {
        try {
            return ImageMetadataReader.readMetadata(image);
        } catch (ImageProcessingException | IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }
}
