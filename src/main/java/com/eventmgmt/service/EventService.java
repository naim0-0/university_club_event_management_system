package com.eventmgmt.service;

import java.sql.SQLException;
import java.util.List;

import com.eventmgmt.dao.EventDAO;
import com.eventmgmt.dao.ClubDAO;
import com.eventmgmt.models.Event;
import com.eventmgmt.patterns.behavioral.observer.EventNotifier;
import com.eventmgmt.patterns.behavioral.state.EventContext;
import com.eventmgmt.patterns.behavioral.state.EventStateFactory;
import com.eventmgmt.patterns.creational.EventBuilder;
import com.eventmgmt.validation.EventValidationChain;
import com.eventmgmt.validation.ValidationHandler;
import com.eventmgmt.models.User;
import com.eventmgmt.security.AuthorizationService;

public class EventService {
    public enum Action { SUBMIT, APPROVE, REJECT }

    private final EventDAO eventDAO;
    private final EventNotifier notifier;
    private final ValidationHandler validators;
    private final ClubDAO clubDAO;
    private final AuthorizationService authorization;
    private final User actor;

    public EventService(EventDAO eventDAO, EventNotifier notifier) {
        this.eventDAO = eventDAO;
        this.notifier = notifier;
        this.validators = EventValidationChain.create();
        this.clubDAO = null;
        this.authorization = null;
        this.actor = null;
    }

    public EventService(EventDAO eventDAO, ClubDAO clubDAO, EventNotifier notifier,
                        AuthorizationService authorization, User actor) {
        this.eventDAO = eventDAO;
        this.clubDAO = clubDAO;
        this.notifier = notifier;
        this.authorization = authorization;
        this.actor = actor;
        this.validators = EventValidationChain.create();
    }

    public List<Event> findAll() {
        try { return eventDAO.getAll(); }
        catch (SQLException e) { throw new BusinessException("Could not load events.", e); }
    }

    public Event create(String title, double budget, int clubId) {
        Event event = new EventBuilder().setTitle(title == null ? null : title.trim())
                .setBudget(budget).setClubId(clubId).build();
        validators.validate(event);
        try {
            if (authorization != null) authorization.requireCreateEvent(actor, clubId, clubDAO.getAll());
            eventDAO.insert(event);
            notifier.notifyAllObservers("New draft created: " + event.getTitle());
            return event;
        } catch (SQLException e) {
            throw new BusinessException("Could not save the event.", e);
        }
    }

    public Event transition(Event event, Action action) {
        if (event == null) throw new BusinessException("Please select an event first.");
        try {
            if (authorization != null) {
                if (action == Action.SUBMIT) authorization.requireSubmit(actor, event, clubDAO.getAll());
                else authorization.requireApproval(actor);
            }
        } catch (SQLException e) { throw new BusinessException("Could not verify event access.", e); }
        EventContext context = new EventContext(event.getId(), EventStateFactory.from(event.getStatus()));
        switch (action) {
            case SUBMIT -> context.submitForApproval();
            case APPROVE -> context.approve();
            case REJECT -> context.reject();
        }
        String status = context.getState().getStatusName();
        try {
            eventDAO.updateStatus(event.getId(), status);
            event.setStatus(status);
            notifier.notifyAllObservers(event.getTitle() + " is now " + status.toLowerCase() + ".");
            return event;
        } catch (SQLException e) {
            throw new BusinessException("Could not update the event status.", e);
        }
    }
}
