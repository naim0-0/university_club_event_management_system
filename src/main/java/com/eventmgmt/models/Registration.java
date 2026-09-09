package com.eventmgmt.models;

public class Registration {
    private int id;
    private int userId;
    private int eventId;
    private double fee;

    public Registration(int id, int userId, int eventId, double fee) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.fee = fee;
    }

    public Registration(int userId, int eventId, double fee) {
        this.userId = userId;
        this.eventId = eventId;
        this.fee = fee;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }
}