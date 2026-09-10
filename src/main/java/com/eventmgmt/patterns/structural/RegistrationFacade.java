package com.eventmgmt.patterns.structural;

import com.eventmgmt.dao.EventDAO;
import com.eventmgmt.dao.RegistrationDAO;
import com.eventmgmt.dao.UserDAO;
import com.eventmgmt.models.Event;
import com.eventmgmt.models.Registration;
import com.eventmgmt.models.User;
import com.eventmgmt.patterns.behavioral.strategy.ClubMemberPricingStrategy;
import com.eventmgmt.patterns.behavioral.strategy.FeeCalculator;
import com.eventmgmt.patterns.behavioral.strategy.RegularPricingStrategy;
import com.eventmgmt.patterns.behavioral.strategy.EarlyBirdPricingStrategy;
import com.eventmgmt.service.BusinessException;

import java.sql.SQLException;
import java.util.List;
import com.eventmgmt.security.AuthorizationService;

public class RegistrationFacade {
    private final UserDAO userDAO;
    private final EventDAO eventDAO;
    private final RegistrationDAO registrationDAO;
    private AuthorizationService authorization;
    private User actor;

    public RegistrationFacade() {
        this.userDAO = new UserDAO();
        this.eventDAO = new EventDAO();
        this.registrationDAO = new RegistrationDAO();
    }

    public RegistrationFacade(UserDAO userDAO, EventDAO eventDAO, RegistrationDAO registrationDAO) {
        this.userDAO = userDAO;
        this.eventDAO = eventDAO;
        this.registrationDAO = registrationDAO;
    }

    public RegistrationFacade(UserDAO userDAO, EventDAO eventDAO, RegistrationDAO registrationDAO,
                              AuthorizationService authorization, User actor) {
        this(userDAO, eventDAO, registrationDAO);
        this.authorization = authorization;
        this.actor = actor;
    }

    public boolean registerUserForEvent(int userId, int eventId, double basePrice) {
        try {
            register(userId, eventId, basePrice, false);
            return true;
        } catch (BusinessException e) {
            return false;
        }
    }

    public Registration register(int userId, int eventId, double basePrice, boolean earlyBird) {
        if (!Double.isFinite(basePrice) || basePrice < 0) throw new BusinessException("Base fee must be positive or zero.");
        if (authorization != null) authorization.requireRegistration(actor, userId);
        try {
            User user = userDAO.getById(userId);
            if (user == null) throw new BusinessException("The selected user no longer exists.");
            Event event = eventDAO.getById(eventId);
            if (event == null) throw new BusinessException("The selected event no longer exists.");
            if (!"APPROVED".equalsIgnoreCase(event.getStatus()))
                throw new BusinessException("Registration is available only for approved events.");
            if (registrationDAO.exists(userId, eventId))
                throw new BusinessException("This user is already registered for the event.");

            FeeCalculator calculator = new FeeCalculator(
                    "PRESIDENT".equalsIgnoreCase(user.getRole()) ? new ClubMemberPricingStrategy()
                            : earlyBird ? new EarlyBirdPricingStrategy() : new RegularPricingStrategy());
            Registration registration = new Registration(userId, eventId, calculator.calculate(basePrice));
            registrationDAO.insert(registration);
            return registration;
        } catch (SQLException e) {
            throw new BusinessException("Could not complete the registration.", e);
        }
    }

    public List<Registration> registrations() {
        try { return registrationDAO.getAll(); }
        catch (SQLException e) { throw new BusinessException("Could not load registrations.", e); }
    }
}
