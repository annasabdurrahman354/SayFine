package com.healthymeals.sayfine.model;

import com.google.firebase.firestore.DocumentId;

public class Menu {
    @DocumentId
    private String id;
    private String title;
    private String description;
    private String thumbUrl;
    private String ShopeeFoodUrl;
    private String GoFoodUrl;
    private String GrabFoodUrl;
    private Integer price;
    private Integer calorie;
    private Boolean type;

    public Menu(String id, String title, String description, String thumbUrl, String shopeeFoodUrl, String goFoodUrl, String grabFoodUrl, Integer price, Integer calorie, Boolean type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbUrl = thumbUrl;
        ShopeeFoodUrl = shopeeFoodUrl;
        GoFoodUrl = goFoodUrl;
        GrabFoodUrl = grabFoodUrl;
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

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getShopeeFoodUrl() {
        return ShopeeFoodUrl;
    }

    public void setShopeeFoodUrl(String shopeeFoodUrl) {
        ShopeeFoodUrl = shopeeFoodUrl;
    }

    public String getGoFoodUrl() {
        return GoFoodUrl;
    }

    public void setGoFoodUrl(String goFoodUrl) {
        GoFoodUrl = goFoodUrl;
    }

    public String getGrabFoodUrl() {
        return GrabFoodUrl;
    }

    public void setGrabFoodUrl(String grabFoodUrl) {
        GrabFoodUrl = grabFoodUrl;
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
