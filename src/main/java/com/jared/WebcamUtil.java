package com.jared;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class WebcamUtil {

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
        FileService.ensureDirectoryExists(DEFAULT_DIRECTORY);

        if (Objects.isNull(webcam)) {
            initWebcam();
        }

        try {
            String filePath = DEFAULT_DIRECTORY + System.currentTimeMillis() + ".png"; 
            ImageIO.write(webcam.getImage(), ImageUtils.FORMAT_PNG, new File(filePath));
            System.out.println("Captured image: " + filePath);
            return new File(filePath);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    
}
