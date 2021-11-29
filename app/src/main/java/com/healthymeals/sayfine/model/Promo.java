package com.healthymeals.sayfine.model;

import com.google.firebase.Timestamp;

public class Promo {
    private String id;
    private String title;
    private String menuId;
    private String thumbUrl;
    private Timestamp timestamp;

    public Promo(String id, String title, String menuId, String thumbUrl, Timestamp timestamp) {
        this.id = id;
        this.title = title;
        this.menuId = menuId;
        this.thumbUrl = thumbUrl;
        this.timestamp = timestamp;
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

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
