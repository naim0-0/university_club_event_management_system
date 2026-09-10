package com.eventmgmt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.eventmgmt.models.Event;
import com.eventmgmt.patterns.behavioral.state.ApprovedState;
import com.eventmgmt.patterns.behavioral.state.DraftState;
import com.eventmgmt.patterns.behavioral.state.EventContext;
import com.eventmgmt.patterns.behavioral.strategy.ClubMemberPricingStrategy;
import com.eventmgmt.patterns.behavioral.strategy.EarlyBirdPricingStrategy;
import com.eventmgmt.patterns.behavioral.strategy.FeeCalculator;
import com.eventmgmt.patterns.creational.EventBuilder;
import com.eventmgmt.service.BusinessException;
import com.eventmgmt.validation.EventValidationChain;

public class PatternWorkflowTest {
    @Test
    public void builderCreatesDraftByDefault() {
        Event event = new EventBuilder().setTitle("Tech Talk").setBudget(1000).setClubId(1).build();
        assertEquals("DRAFT", event.getStatus());
    }

    @Test
    public void stateAllowsDraftToPendingToApproved() {
        EventContext context = new EventContext(1, new DraftState());
        context.submitForApproval();
        assertEquals("PENDING", context.getState().getStatusName());
        context.approve();
        assertEquals("APPROVED", context.getState().getStatusName());
    }

    @Test
    public void stateRejectsInvalidTransition() {
        EventContext context = new EventContext(1, new ApprovedState());
        assertThrows(BusinessException.class, context::reject);
    }

    @Test
    public void pricingStrategiesRemainInterchangeable() {
        FeeCalculator calculator = new FeeCalculator(new EarlyBirdPricingStrategy());
        assertEquals(900, calculator.calculate(1000), 0.001);
        calculator.setStrategy(new ClubMemberPricingStrategy());
        assertEquals(800, calculator.calculate(1000), 0.001);
    }

    @Test
    public void validationChainRejectsNegativeBudget() {
        Event invalid = new Event("Valid title", -1, "DRAFT", 1);
        assertThrows(BusinessException.class, () -> EventValidationChain.create().validate(invalid));
    }
}
