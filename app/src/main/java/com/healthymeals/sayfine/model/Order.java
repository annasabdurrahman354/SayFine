package com.healthymeals.sayfine.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;


public class Order {
    private String id;
    private String userId;
    private String menuId;
    private Boolean finish;
    private Timestamp dateOrder;

    public Order(String id, String userId, String menuId, Boolean finish, Timestamp dateOrder) {
        this.id = id;
        this.userId = userId;
        this.menuId = menuId;
        this.finish = finish;
        this.dateOrder = dateOrder;
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

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public Timestamp getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(Timestamp dateOrder) {
        this.dateOrder = dateOrder;
    }
}
