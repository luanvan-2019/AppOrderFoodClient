package com.hcmunre.apporderfoodclient.models.Entity;

public class ChatMessage {

    private String userId;
    private String restaurantId;
    private String message;
    private long time;

    public ChatMessage(String userId, String restaurantId, String message, long time) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.message = message;
        this.time = time;
    }

    public ChatMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}