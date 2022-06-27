package com.jared.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.file.FileSystemDirectory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class ImageUtil {

    public static String DEFAULT_FORMAT = "hh:mma";

    public static File timestampImage(File file) {
        return timestampImage(file, DEFAULT_FORMAT);
    }

    public static File timestampImage(File file, String format) {
        try {
            long millis = getLastModifiedMillis(file);
            String text = "Day " + calcDaysBetweenMillis(millis, 1642737276840L) + " - " + Utils.millisToDateStr(millis, format);

            BufferedImage bufferedImage = ImageIO.read(file);
            Graphics graphics = bufferedImage.getGraphics();
            Font font = new Font("Arial", Font.BOLD, 48);
            graphics.setFont(font);
            graphics.setColor(Color.RED);
            graphics.drawString(text, 50, 200);
            graphics.dispose();
            ImageIO.write(bufferedImage, "png", file);
            System.out.println("Timestamped image: " + file.getName() + " -> " + text);
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


    private static long getLastModifiedMillis(File image) {
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
