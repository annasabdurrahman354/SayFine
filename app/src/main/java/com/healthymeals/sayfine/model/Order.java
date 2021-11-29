package com.healthymeals.sayfine.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;


public class Order {
    private String id;
    private String userId;
    private String menuId;
    private String menuName;
    private Integer orderBy;
    private Boolean verified;
    private Timestamp timestamp;

    public Order(String id, String userId, String menuId, String menuName, Integer orderBy, Boolean verified, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.menuId = menuId;
        this.menuName = menuName;
        this.orderBy = orderBy;
        this.verified = verified;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
