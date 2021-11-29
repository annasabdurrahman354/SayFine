package com.healthymeals.sayfine.model;

import com.google.firebase.Timestamp;

public class ChatRoom {
    private String id;
    private String userName;
    private Timestamp lastChat;

    public ChatRoom(String id, String userName, Timestamp lastChat) {
        this.id = id;
        this.userName = userName;
        this.lastChat = lastChat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getLastChat() {
        return lastChat;
    }

    public void setLastChat(Timestamp lastChat) {
        this.lastChat = lastChat;
    }
}
