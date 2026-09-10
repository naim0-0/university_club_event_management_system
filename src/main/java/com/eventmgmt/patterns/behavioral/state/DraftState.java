package com.eventmgmt.patterns.behavioral.state;

import com.eventmgmt.service.BusinessException;

public class DraftState implements EventState {
    @Override
    public void submitForApproval(EventContext context) {
        context.setState(new PendingState());
    }

    @Override
    public void approve(EventContext context) {
        throw new BusinessException("Draft events must be submitted before approval.");
    }

    @Override
    public void reject(EventContext context) {
        throw new BusinessException("A draft event cannot be rejected.");
    }

    @Override
    public String getStatusName() {
        return "DRAFT";
    }
}
