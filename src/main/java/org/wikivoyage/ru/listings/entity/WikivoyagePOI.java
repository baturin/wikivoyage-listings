package org.wikivoyage.ru.listings.entity;

public class WikivoyagePOI {
    private float latitude;
    private float longitude;
    private String title;
    private String description;
    private String type;

    public WikivoyagePOI(String type, String title, String description, float latitude, float longitude) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String humanReadable()
    {
        return "type=" + type + "|title=" + title + "|lat=" + latitude + "|long=" + longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
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
}
