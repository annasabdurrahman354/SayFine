package com.healthymeals.sayfine.model;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentId;

public class User {
    @DocumentId
    private String id;
    private String name;
    private String profileUrl;
    private String phoneNumber;
    private String mainAddressId;

    public User(String id, String name, @Nullable String profileUrl, String phoneNumber, @Nullable String mainAddressId) {
        this.id = id;
        this.name = name;
        this.profileUrl = profileUrl;
        this.phoneNumber = phoneNumber;
        this.mainAddressId = mainAddressId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMainAddressId() {
        return mainAddressId;
    }

    public void setMainAddressId(String mainAddressId) {
        this.mainAddressId = mainAddressId;
    }
}
