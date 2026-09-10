package com.eventmgmt.patterns.behavioral.state;

import com.eventmgmt.service.BusinessException;

public final class EventStateFactory {
    private EventStateFactory() {}

    public static EventState from(String status) {
        return switch (status.toUpperCase()) {
            case "DRAFT" -> new DraftState();
            case "PENDING" -> new PendingState();
            case "APPROVED" -> new ApprovedState();
            case "REJECTED" -> new RejectedState();
            default -> throw new BusinessException("Unknown event status: " + status);
        };
    }
}
