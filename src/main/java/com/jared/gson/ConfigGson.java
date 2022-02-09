package com.jared.gson;

import com.google.gson.annotations.SerializedName;

public class ConfigGson {
    
    @SerializedName("outputDirectory")
    public String outputDirectory;

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }
}
