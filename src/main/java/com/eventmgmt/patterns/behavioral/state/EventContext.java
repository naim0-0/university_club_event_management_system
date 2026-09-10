package com.eventmgmt.patterns.behavioral.state;

public class EventContext {
    private EventState currentState;
    private final int eventId;

    public EventContext(int eventId, EventState initialState) {
        this.eventId = eventId;
        this.currentState = initialState;
    }

    public void setState(EventState state) {
        this.currentState = state;
    }

    public EventState getState() {
        return currentState;
    }

    public int getEventId() {
        return eventId;
    }

    public void submitForApproval() {
        currentState.submitForApproval(this);
    }

    public void approve() {
        currentState.approve(this);
    }

    public void reject() {
        currentState.reject(this);
    }
}