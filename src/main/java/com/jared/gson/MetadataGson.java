package com.jared.gson;

import com.google.gson.annotations.SerializedName;

public class MetadataGson {
    
    @SerializedName("startTimeMillis")
    private long startTimeMillis; 
    
    @SerializedName("projectName")
    private String projectName;
    
    @SerializedName("intervalMinutes")
    private int intervalMinutes;
    
    @SerializedName("projectDirectory")
    private String projectDirectory;

    public String getProjectDirectory() {
        return projectDirectory;
    }

    public void setProjectDirectory(String projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public void setStartTimeMillis(long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getIntervalMinutes() {
        return intervalMinutes;
    }

    public void setIntervalMinutes(int intervalMinutes) {
        this.intervalMinutes = intervalMinutes;
    }
}
