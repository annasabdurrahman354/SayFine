package com.healthymeals.sayfine.model;

import com.google.firebase.firestore.DocumentId;

public class Promo {
    private String id;
    private String title;
    private String description;
    private String thumbUrl;

    public Promo(String id, String title, String description, String thumbUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbUrl = thumbUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}
