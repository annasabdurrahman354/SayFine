package com.healthymeals.sayfine.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

public class BodyMassIndex {
    private String id;
    private Float height;
    private Float mass;
    private Timestamp timestamp;

    public BodyMassIndex(String id, Float height, Float mass, Timestamp timestamp) {
        this.id = id;
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
