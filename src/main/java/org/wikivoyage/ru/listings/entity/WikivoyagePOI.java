package org.wikivoyage.ru.listings.entity;

public class WikivoyagePOI {
    private String latitude;
    private String longitude;
    private String article;
    private String title;
    private String description;
    private String type;
    private String url;

    public WikivoyagePOI(
        String article, String type, String title, String description, String latitude, String longitude, String url
    ) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.article = article;
        this.url =url;
    }

    public String humanReadable()
    {
        return "type=" + type + "|title=" + title + "|lat=" + latitude + "|long=" + longitude + "|url=" + url;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getArticle()
    {
        return article;
    }
    
    public String getURL()
    {
        return url;
    }
}
