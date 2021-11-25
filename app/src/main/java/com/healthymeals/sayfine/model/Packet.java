package com.healthymeals.sayfine.model;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;

public class Packet {
    private String id;
    private String title;
    private String description;
    private String thumbUrl;
    private ArrayList<String> menuIDs;

    public Packet(String id, String title, String description, String thumbUrl, ArrayList<String> menuIDs) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbUrl = thumbUrl;
        this.menuIDs = menuIDs;
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

    public ArrayList<String> getMenuIDs() {
        return menuIDs;
    }

    public void setMenuIDs(ArrayList<String> menuIDs) {
        this.menuIDs = menuIDs;
    }
}
