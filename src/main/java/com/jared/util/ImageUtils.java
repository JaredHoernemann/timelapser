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

public class ImageUtils {

    public static String DEFAULT_FORMAT = "hh:mma";


    public static File cropImage(File file) throws Exception{
        BufferedImage bufferedImage = ImageIO.read(file);
//        BufferedImage cropped = bufferedImage.getSubimage(0,400, 2560, 1440); // control
        BufferedImage cropped = bufferedImage.getSubimage(2912,400, 2560, 1440); // BIOMANN
        ImageIO.write(cropped, "JPG", file);
        System.out.println("Cropped image: " + file.getAbsolutePath());
        return file;

    }

    public static File writeTextToImage(File file, String text) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            Graphics graphics = bufferedImage.getGraphics();
            Font font = new Font("Arial", Font.BOLD, 144);
            graphics.setFont(font);
            graphics.setColor(Color.RED);
            graphics.drawString(text, 100, 100);
            graphics.dispose();
            ImageIO.write(bufferedImage, "JPG", file);
            System.out.println("Wrote text to image: " + file.getName() + " -> " + text);
            return file;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public static int calcMinutesBetweenMillis(long first, long second) {
        long elapsed = Math.abs(first - second); //order of params is irrelevant
        long millisInMinute = 1000 * 60;
        int count = 0;
        for (long x = elapsed; x >= millisInMinute; x -= millisInMinute) {
            count++;
        }
        return count;
    }

    public static int calcHoursBetweenMillis(long first, long second) {
        long elapsed = Math.abs(first - second); //order of params is irrelevant
        long millisInHour = 1000 * 60 * 60;
        int count = 0;
        for (long x = elapsed; x >= millisInHour; x -= millisInHour) {
            count++;
        }
        return count;
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


    public static boolean isLightOn(File file) {
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
