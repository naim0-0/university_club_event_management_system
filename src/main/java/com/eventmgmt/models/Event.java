package com.eventmgmt.models;

public class Event {
    private int id;
    private String title;
    private double budget;
    private String status; // DRAFT, PENDING, APPROVED, REJECTED
    private int clubId;

    public Event(int id, String title, double budget, String status, int clubId) {
        this.id = id;
        this.title = title;
        this.budget = budget;
        this.status = status;
        this.clubId = clubId;
    }

    public Event(String title, double budget, String status, int clubId) {
        this.title = title;
        this.budget = budget;
        this.status = status;
        this.clubId = clubId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getClubId() { return clubId; }
    public void setClubId(int clubId) { this.clubId = clubId; }

    @Override
    public String toString() { return title + " (" + status + ")"; }
}
