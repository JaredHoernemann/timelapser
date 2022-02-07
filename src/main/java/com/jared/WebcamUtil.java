package com.jared;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class WebcamUtil {

    private static final String DEFAULT_DIRECTORY = "target/webcam-captures/";
    private static final Dimension RESOLUTION = new Dimension(1920, 1080);
    
    public static File takePicture() {
        FileService.ensureDirectoryExists(DEFAULT_DIRECTORY);

        Webcam webcam = Webcam.getDefault();
        
        if (webcam == null) {
            throw new IllegalStateException("Failed to connect to webcam");
        }

        webcam.setCustomViewSizes(RESOLUTION);
        webcam.setViewSize(new Dimension(RESOLUTION));
        webcam.open();

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
