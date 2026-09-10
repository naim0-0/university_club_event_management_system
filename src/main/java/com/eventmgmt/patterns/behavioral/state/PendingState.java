package com.eventmgmt.patterns.behavioral.state;

import com.eventmgmt.service.BusinessException;

public class PendingState implements EventState {
    @Override
    public void submitForApproval(EventContext context) {
        throw new BusinessException("The event is already awaiting approval.");
    }

    @Override
    public void approve(EventContext context) {
        context.setState(new ApprovedState());
    }

    @Override
    public void reject(EventContext context) {
        context.setState(new RejectedState());
    }

    @Override
    public String getStatusName() {
        return "PENDING";
    }
}
