package com.eventmgmt.validation;

import com.eventmgmt.models.Event;
import com.eventmgmt.service.BusinessException;

/** A Chain of Responsibility node for event validation. */
public abstract class ValidationHandler {
    private ValidationHandler next;

    public ValidationHandler linkWith(ValidationHandler next) {
        this.next = next;
        return next;
    }

    public final void validate(Event event) {
        check(event);
        if (next != null) next.validate(event);
    }

    protected abstract void check(Event event) throws BusinessException;
}
