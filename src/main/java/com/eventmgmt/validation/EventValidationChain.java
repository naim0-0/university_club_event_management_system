package com.eventmgmt.validation;

public final class EventValidationChain {
    private EventValidationChain() {}

    public static ValidationHandler create() {
        ValidationHandler first = new EventTitleValidator();
        first.linkWith(new EventBudgetValidator()).linkWith(new EventClubValidator());
        return first;
    }
}
