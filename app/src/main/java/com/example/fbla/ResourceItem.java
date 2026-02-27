package com.example.fbla;

public class ResourceItem {

    public String title;
    public String description;
    public String type; // "Study Guide", "Video", "Rubric"
    public String fileName;
    public int downloads;

    public ResourceItem(String title, String description, String type, String fileName, int downloads) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.fileName = fileName;
        this.downloads = downloads;
    }
}
