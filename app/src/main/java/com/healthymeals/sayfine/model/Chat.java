package com.healthymeals.sayfine.model;

import com.google.firebase.Timestamp;

public class Chat {
    private String id;
    private Boolean isCustomer;
    private String text;
    private Timestamp timestamp;

    public Chat(String id, Boolean isCustomer, String text, Timestamp timestamp) {
        this.id = id;
        this.isCustomer = isCustomer;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getCustomer() {
        return isCustomer;
    }

    public void setCustomer(Boolean customer) {
        isCustomer = customer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
