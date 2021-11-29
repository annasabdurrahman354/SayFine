package com.healthymeals.sayfine.model;

import com.google.firebase.Timestamp;

public class Chat {
    private String id;
    private String userId;
    private String text;
    private Timestamp timestamp;

    public Chat(String id, String userId, String text, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.text = text;
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
