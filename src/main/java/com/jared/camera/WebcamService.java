package com.jared.camera;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.util.ImageUtils;
import com.jared.util.FileService;
import com.jared.util.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class WebcamService {

    private static final String DEFAULT_DIRECTORY = "target/webcam-captures/";
    private static final Dimension RESOLUTION = new Dimension(1920, 1080);
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
        return takePicture(DEFAULT_DIRECTORY);
    }
    
    public static File takePicture(String toDir) {
        FileService.ensureDirectoryExists(toDir);

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
