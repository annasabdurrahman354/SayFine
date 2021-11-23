package com.healthymeals.sayfine.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

public class BodyMassIndex {
    @DocumentId
    private String id;
    private String userId;
    private Float mass;
    private Float height;
    private Timestamp timestamp;

    public BodyMassIndex(String id, String userId, Float mass, Float height, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.mass = mass;
        this.height = height;
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

    public Float getMass() {
        return mass;
    }

    public void setMass(Float mass) {
        this.mass = mass;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
