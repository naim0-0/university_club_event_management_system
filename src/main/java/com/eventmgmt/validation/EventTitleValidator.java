package com.eventmgmt.validation;

import com.eventmgmt.models.Event;
import com.eventmgmt.service.BusinessException;

public class EventTitleValidator extends ValidationHandler {
    @Override
    protected void check(Event event) {
        if (event.getTitle() == null || event.getTitle().isBlank()) {
            throw new BusinessException("Event title is required.");
        }
        if (event.getTitle().trim().length() < 3) {
            throw new BusinessException("Event title must contain at least 3 characters.");
        }
    }
}
