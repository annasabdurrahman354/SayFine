package com.healthymeals.sayfine.model;

import com.google.firebase.firestore.DocumentId;

public class Menu {
    private String id;
    private String title;
    private String description;
    private String goFoodUrl;
    private String grabFoodUrl;
    private String shopeeFoodUrl;
    private String thumbUrl;
    private Integer price;
    private Integer calorie;
    private Boolean type;

    public Menu(String id, String title, String description, String goFoodUrl, String grabFoodUrl, String shopeeFoodUrl, String thumbUrl, Integer price, Integer calorie, Boolean type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.goFoodUrl = goFoodUrl;
        this.grabFoodUrl = grabFoodUrl;
        this.shopeeFoodUrl = shopeeFoodUrl;
        this.thumbUrl = thumbUrl;
        this.price = price;
        this.calorie = calorie;
        this.type = type;
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

    public String getGoFoodUrl() {
        return goFoodUrl;
    }

    public void setGoFoodUrl(String goFoodUrl) {
        this.goFoodUrl = goFoodUrl;
    }

    public String getGrabFoodUrl() {
        return grabFoodUrl;
    }

    public void setGrabFoodUrl(String grabFoodUrl) {
        this.grabFoodUrl = grabFoodUrl;
    }

    public String getShopeeFoodUrl() {
        return shopeeFoodUrl;
    }

    public void setShopeeFoodUrl(String shopeeFoodUrl) {
        this.shopeeFoodUrl = shopeeFoodUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCalorie() {
        return calorie;
    }

    public void setCalorie(Integer calorie) {
        this.calorie = calorie;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }
}
