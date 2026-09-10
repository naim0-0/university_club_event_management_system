package com.eventmgmt.validation;

import com.eventmgmt.models.Event;
import com.eventmgmt.service.BusinessException;

public class EventClubValidator extends ValidationHandler {
    @Override
    protected void check(Event event) {
        if (event.getClubId() <= 0) throw new BusinessException("Please select an organizing club.");
    }
}
