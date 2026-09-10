package com.eventmgmt.patterns.behavioral.state;

import com.eventmgmt.service.BusinessException;

public class ApprovedState implements EventState {
    @Override
    public void submitForApproval(EventContext context) {
        throw new BusinessException("An approved event cannot be submitted again.");
    }

    @Override
    public void approve(EventContext context) {
        throw new BusinessException("The event is already approved.");
    }

    @Override
    public void reject(EventContext context) {
        throw new BusinessException("An approved event cannot be rejected.");
    }

    @Override
    public String getStatusName() {
        return "APPROVED";
    }
}
