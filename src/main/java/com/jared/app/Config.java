package com.jared.app;

import com.google.common.base.Strings;
import com.jared.utils.FileUtils;

import java.io.*;
import java.util.Properties;

public class Config {

    private static final String DEFAULT_OUT_DIR = System.getProperty("user.home") + "\\OneDrive\\TimelapseVideos\\";
    private static final Properties CONFIG;

    static {
        /*
        Create a default config file if one doesn't already exist for the Client
         */
        if (ConfigFile.doesNotExist()) {
            ConfigFile.createFileWithDefaults();
        }
        CONFIG = ConfigFile.getPropertiesFromFile();
    }

    public static Boolean timestampImages() {
        return Boolean.parseBoolean(getNonNullOrEmptyProperty(Keys.TIMESTAMP_IMAGES));
    }

    public static Boolean cropImages() {
        return Boolean.parseBoolean(getNonNullOrEmptyProperty(Keys.CROP_IMAGES));
    }

    public static int getCropStartX() {
        return Integer.parseInt(getNonNullOrEmptyProperty(Keys.CROP_START_X));
    }

    public static int getCropStartY() {
        return Integer.parseInt(getNonNullOrEmptyProperty(Keys.CROP_START_Y));
    }

    public static int getCropWidthNumPixels() {
        return Integer.parseInt(getNonNullOrEmptyProperty(Keys.CROP_WIDTH_NUM_PIXELS));
    }

    public static int getCropHeightNumPixels() {
        return Integer.parseInt(getNonNullOrEmptyProperty(Keys.CROP_HEIGHT_NUM_PIXELS));
    }

    public static String getTimelapseResolution() {
        return getNonNullOrEmptyProperty(Keys.TIMELAPSE_RESOLUTION);
    }

    public static int getTimelapseFrameRate() {
        return Integer.parseInt(getNonNullOrEmptyProperty(Keys.TIMELAPSE_FRAME_RATE));
    }

    public static int getTimelapseResolutionWidth() {
        return Integer.parseInt(getNonNullOrEmptyProperty(Keys.TIMELAPSE_RES_WIDTH));
    }

    public static int getTimelapseResolutionHeight() {
        return Integer.parseInt(getNonNullOrEmptyProperty(Keys.TIMELAPSE_RES_HEIGHT));
    }

    public static String getTimelapseOutputDirectory() {
        return getNonNullOrEmptyProperty(Keys.TIMELAPSE_OUT_DIR);
    }

    public static String getTimelapsePicturesDirectory() {
        return getNonNullOrEmptyProperty(Keys.TIMELAPSE_PICS_DIR);
    }

    public static Integer getTimelapseNumHours() {
        String value = CONFIG.getProperty(Keys.TIMELAPSE_NUM_HOURS, null);
        if (Strings.isNullOrEmpty(value)) {
            return null;
        }
        return Integer.parseInt(value);
    }

    public static String getTimelapseFileName() {
        return getNonNullOrEmptyProperty(Keys.TIMELAPSE_NAME);
    }

    public static int getWebcamResolutionHeight() {
        return Integer.parseInt(getNonNullOrEmptyProperty(Keys.WEBCAM_RES_HEIGHT));
    }

    public static int getWebcamResolutionWidth() {
        return Integer.parseInt(getNonNullOrEmptyProperty(Keys.WEBCAM_RES_WIDTH));
    }

    public static int getWebcamPictureIntervalMinutes() {
        return Integer.parseInt(getNonNullOrEmptyProperty(Keys.WEBCAM_PIC_INTERVAL_MINUTES));
    }

    public static int getTimstampFontSize() {
        String value = CONFIG.getProperty(Keys.TIMESTAMP_FONT_SIZE, "48");
        return Integer.parseInt(value);
    }

    public static int getTimestampPositionX() {
        String value = CONFIG.getProperty(Keys.TIMESTAMP_POSITION_X, "75");
        return Integer.parseInt(value);
    }

    public static int getTimestampPositionY() {
        String value = CONFIG.getProperty(Keys.TIMESTAMP_Y_VALUE, "75");
        return Integer.parseInt(value);
    }

    private static String getNonNullOrEmptyProperty(String propertyKey) {
        String value = CONFIG.getProperty(propertyKey);
        if (Strings.isNullOrEmpty(value)) {
            throw new NullPointerException("Config value is null or empty: " + propertyKey);
        } else {
            return value;
        }
    }

    public static class ConfigFile {
        private static final String DIRECTORY = "src/main/resources/";
        private static final String NAME = "config.properties";
        private static final String FILEPATH = DIRECTORY + NAME;

        /**
         * Returns a {@link Properties} object from the file at {@link #FILEPATH}.
         *
         * @return A {@link  Properties} file.
         */
        static Properties getPropertiesFromFile() {
            Properties properties = new Properties();

            try {
                InputStream inputStream = new FileInputStream(new File(FILEPATH));
                properties.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return properties;
        }

        /**
         * Returns True if a file exists at the file path {@link #FILEPATH}.
         *
         * @return Boolean true or false.
         */
        static boolean doesNotExist() {
            return !new File(FILEPATH).exists();
        }

        /**
         * Writes a {@link Properties} file to {@link #FILEPATH} using default values {@link #getDefaultProperties()}.
         */
        static void createFileWithDefaults() {
            OutputStream outputStream = null;

            try {
                FileUtils.ensureDirectoryExists(DIRECTORY);
                outputStream = new FileOutputStream(FILEPATH);
                getDefaultProperties().store(outputStream, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally { //always close the stream
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            }
        }


        /**
         * Returns a {@link Properties} object with default keys and values.
         *
         * @return {@link Properties} object.
         */
        private static Properties getDefaultProperties() {
            Properties defaults = new Properties();
            defaults.setProperty(Keys.TIMELAPSE_OUT_DIR, DEFAULT_OUT_DIR);
            defaults.setProperty(Keys.TIMELAPSE_NUM_HOURS, String.valueOf(24));
            defaults.setProperty(Keys.TIMELAPSE_RESOLUTION, "2560x1700");
            defaults.setProperty(Keys.TIMELAPSE_PICS_DIR, "");
            defaults.setProperty(Keys.TIMELAPSE_FRAME_RATE, String.valueOf(28));
            defaults.setProperty(Keys.CROP_IMAGES, String.valueOf(false));
            return defaults;
        }
    }

    static final class Keys {

        static final String TIMELAPSE_OUT_DIR = "timelapseOutputDirectory";
        static final String TIMELAPSE_NUM_HOURS = "timelapseNumHours";
        static final String TIMELAPSE_PICS_DIR = "timelapsePicturesDirectory";
        static final String TIMELAPSE_FRAME_RATE = "timelapseFrameRate";
        static final String TIMELAPSE_RESOLUTION = "timelapseResolution";
        static final String TIMELAPSE_RES_WIDTH = "timelapseResolutionWidth";
        static final String TIMELAPSE_RES_HEIGHT = "timelapseResolutionHeight";
        static final String CROP_IMAGES = "cropImages";
        static final String CROP_START_X = "cropStartX";
        static final String CROP_START_Y = "cropStartY";
        static final String CROP_WIDTH_NUM_PIXELS = "cropWidthNumPixels";
        static final String CROP_HEIGHT_NUM_PIXELS = "cropHeightNumPixels";
        static final String TIMESTAMP_IMAGES = "timestampImages";
        static final String TIMELAPSE_NAME = "timelapseFileName";
        static final String WEBCAM_RES_WIDTH = "webcamResolutionWidth";
        static final String WEBCAM_RES_HEIGHT = "webcamResolutionHeight";
        static final String WEBCAM_PIC_INTERVAL_MINUTES = "webcamPictureIntervalMinutes";
        static final String TIMESTAMP_FONT_SIZE = "timestampFontSize";
        static final String TIMESTAMP_POSITION_X = "timestampPositionX";
        static final String TIMESTAMP_Y_VALUE = "timestampPositionY";
    }
}
