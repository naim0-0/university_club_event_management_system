package com.eventmgmt.validation;

import com.eventmgmt.models.Event;
import com.eventmgmt.service.BusinessException;

public class EventBudgetValidator extends ValidationHandler {
    @Override
    protected void check(Event event) {
        if (!Double.isFinite(event.getBudget()) || event.getBudget() < 0) {
            throw new BusinessException("Budget must be a positive number or zero.");
        }
    }
}
