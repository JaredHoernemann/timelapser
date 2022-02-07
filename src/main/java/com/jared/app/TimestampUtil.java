package com.jared.app;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.file.FileSystemDirectory;
import com.jared.Utils;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimestampUtil {
    
    public static String DEFAULT_FORMAT = "MMMM dd: h:mma";
    
    public static List<File> timestampImages(List<File> files) {
        List<File> timestampedFiles = new ArrayList<>();
        for (File f : files) {
            File temp = timestampImage(f);
            timestampedFiles.add(temp);
        }
        return timestampedFiles;
    }
    
    public static File timestampImage(File file) {
        return timestampImage(file, DEFAULT_FORMAT);
    }
    
    
    public static File timestampImage(File file, String format) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            
            Font font = new Font("Arial", Font.BOLD, 72);
            long millis = getLastModifiedMillis(file);
            String timestamp = Utils.millisToDateStr(millis, format);
            
            Graphics graphics = bufferedImage.getGraphics();
            graphics.setFont(font);
            graphics.setColor(Color.RED);
            graphics.drawString(timestamp, 150, 150);
            graphics.dispose();
            ImageIO.write(bufferedImage, "jpg", file);
            System.out.println("Timestamped image: " + file.getName() + " -> " + timestamp);
            return file;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
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
