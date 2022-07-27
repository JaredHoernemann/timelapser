package com.jared.camera;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.util.ImageUtils;
import com.jared.app.Config;
import com.jared.utils.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class WebcamCamera {

    private static final String PICTURES_DIR = Config.getWebcamPicturesDirectory();
    private static final Dimension RESOLUTION = new Dimension(Config.getWebcamResolutionWidth(), Config.getWebcamResolutionHeight());
    private static Webcam webcam;

    private static void initWebcam() {
        webcam = Webcam.getDefault();

        if (webcam == null) {
            throw new IllegalStateException("Failed to connect to webcam");
        }
        webcam.setCustomViewSizes(RESOLUTION);
        webcam.setViewSize(new Dimension(RESOLUTION));
        webcam.open();
    }

    public static File takePicture() {
        return takePicture(PICTURES_DIR);
    }
    
    public static File takePicture(String toDir) {
        FileUtils.ensureDirectoryExists(toDir);

        if (Objects.isNull(webcam)) {
            initWebcam();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            String filePath = toDir + System.currentTimeMillis() + ".png";
            BufferedImage image = webcam.getImage();
            ImageIO.write(image, ImageUtils.FORMAT_PNG, new File(filePath));
            System.out.println("Captured image: " + filePath);
            return new File(filePath);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    
}
