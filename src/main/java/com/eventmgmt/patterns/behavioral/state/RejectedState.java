package com.eventmgmt.patterns.behavioral.state;

import com.eventmgmt.service.BusinessException;

public class RejectedState implements EventState {
    @Override
    public void submitForApproval(EventContext context) {
        context.setState(new PendingState());
    }

    @Override
    public void approve(EventContext context) {
        throw new BusinessException("Resubmit a rejected event before approving it.");
    }

    @Override
    public void reject(EventContext context) {
        throw new BusinessException("The event is already rejected.");
    }

    @Override
    public String getStatusName() {
        return "REJECTED";
    }
}
