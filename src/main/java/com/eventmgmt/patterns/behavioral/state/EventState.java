package com.eventmgmt.patterns.behavioral.state;

public interface EventState {
    void submitForApproval(EventContext context);
    void approve(EventContext context);
    void reject(EventContext context);
    String getStatusName();
}