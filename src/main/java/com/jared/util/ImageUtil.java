package com.jared.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.file.FileSystemDirectory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class ImageUtil {

    public static String DEFAULT_FORMAT = "MMMM dd - hh:mma";

    public static File timestampImage(File file) {
        return timestampImage(file, DEFAULT_FORMAT);
    }

    public static File timestampImage(File file, String format) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);

            Font font = new Font("Arial", Font.BOLD, 48);
            long millis = getLastModifiedMillis(file);
            String timestamp = Utils.millisToDateStr(millis, format)
                    + "     (Day " + calcDaysBetweenMillis(millis, 1642737276840L) + ")";

            Graphics graphics = bufferedImage.getGraphics();
            graphics.setFont(font);
            graphics.setColor(Color.RED);
            graphics.drawString(timestamp, 50, 500);
            graphics.dispose();
            ImageIO.write(bufferedImage, "png", file);
            System.out.println("Timestamped image: " + file.getName() + " -> " + timestamp);
            return file;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public static int calcDaysBetweenMillis(long first, long second) {
        long elapsed = Math.abs(first - second); //order of params is irrelevant 
        long millisInDay = 1000 * 60 * 60 * 24;

        int count = 0;
        for (long x = elapsed; x >= millisInDay; x -= millisInDay) {
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


    public static boolean lightsAreOn(File file) {
        int x;
        int pixel;

        try {
            BufferedImage image = ImageIO.read(file);

            int y = (int) Math.round(1.0 * image.getHeight() / 2); // vertical mid-point of image

            for (x = 0; x < image.getWidth(); x++) {
                pixel = image.getRGB(x, y);
                int r, g, b;
                r = getRed(pixel);
                g = getGreen(pixel);
                b = getBlue(pixel);

                if (r > 50 || g > 50 || b > 50) {
                    System.out.println(file.getName() + " -> Lights are on");
                    return true;
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
        System.out.println(file.getName() + " -> Lights are off");
        return false;
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