package com.example.fbla;

public class NewsItem {

    private final boolean featured;
    private final String category;
    private final String title;
    private final String description;
    private final String date;
    private final String image;
    private final String link;

    public NewsItem(
            boolean featured,
            String category,
            String title,
            String description,
            String date,
            String image,
            String link
    ) {
        this.featured = featured;
        this.category = category;
        this.title = title;
        this.description = description;
        this.date = date;
        this.image = image;
        this.link = link;
    }

    public boolean isFeatured() {
        return featured;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getLink() {
        return link;
    }
}
