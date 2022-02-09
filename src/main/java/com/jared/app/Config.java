package com.jared.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jared.gson.ConfigGson;
import com.jared.util.FileService;

public class Config {

    private static final String JSON_PATH = "src/main/resources/config.json";
    private static final String DEFAULT_OUT_DIR = System.getProperty("user.home") + "\\Timelapse\\";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    private ConfigGson configGson;

    private Config()  {
        FileService.ensureFilePathExists(JSON_PATH);
        configGson = gson.fromJson(FileService.readFileAsString(JSON_PATH), ConfigGson.class);
        if (configGson ==  null) { //no config file exists, create one
            configGson = new ConfigGson();
            configGson.setOutputDirectory(DEFAULT_OUT_DIR);
            FileService.writeToFile(gson.toJson(configGson), JSON_PATH);
        }
    }
    
    public static Config get() {
        return new Config();
    }
    
    public String outputDirectory() {
        return this.configGson.getOutputDirectory();
    }
}
