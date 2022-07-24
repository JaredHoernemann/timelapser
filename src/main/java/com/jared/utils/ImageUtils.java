package com.jared.utils;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    public static File cropImage(File file, int x, int y, int w, int h) throws Exception{
        BufferedImage bufferedImage = ImageIO.read(file);
        BufferedImage cropped = bufferedImage.getSubimage(x,y, w, h);
        ImageIO.write(cropped, "JPG", file);
        System.out.println("Cropped image: " + file.getAbsolutePath());
        return file;

    }

    public static File writeTextToImage(File file, String text) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            Graphics graphics = bufferedImage.getGraphics();
            Font font = new Font("Arial", Font.BOLD, 48);
            graphics.setFont(font);
            graphics.setColor(Color.WHITE);
            graphics.drawString(text, 75, 75);
            graphics.dispose();
            ImageIO.write(bufferedImage, "JPG", file);
            System.out.println("Wrote text to image: " + file.getName() + ": \"" + text + "\"");
            return file;
        } catch (IOException e) {
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

                if (r > 25 || g > 25 || b > 25) {
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
