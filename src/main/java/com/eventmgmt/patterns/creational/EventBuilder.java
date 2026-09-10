package com.eventmgmt.patterns.creational;

import com.eventmgmt.models.Event;

public class EventBuilder {
    private int id;
    private String title;
    private double budget;
    private String status = "DRAFT";
    private int clubId;

    public EventBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public EventBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public EventBuilder setBudget(double budget) {
        this.budget = budget;
        return this;
    }

    public EventBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    public EventBuilder setClubId(int clubId) {
        this.clubId = clubId;
        return this;
    }

    public Event build() {
        return new Event(id, title, budget, status, clubId);
    }
}
