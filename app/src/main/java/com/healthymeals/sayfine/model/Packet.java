package com.healthymeals.sayfine.model;

import java.util.ArrayList;

public class Packet {
    private String id;
    private String title;
    private String description;
    private String thumbUrl;
    private ArrayList<String> menuIdList;

    public Packet(String id, String title, String description, String thumbUrl, ArrayList<String> menuIdList) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbUrl = thumbUrl;
        this.menuIdList = menuIdList;
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

    public ArrayList<String> getMenuIdList() {
        return menuIdList;
    }

    public void setMenuIdList(ArrayList<String> menuIdList) {
        this.menuIdList = menuIdList;
    }
}
