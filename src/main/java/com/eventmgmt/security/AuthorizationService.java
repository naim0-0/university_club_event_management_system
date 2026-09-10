package com.eventmgmt.security;

import com.eventmgmt.models.Club;
import com.eventmgmt.models.Event;
import com.eventmgmt.models.User;
import com.eventmgmt.service.BusinessException;
import java.util.List;

public class AuthorizationService {
    public void requireCreateEvent(User actor, int clubId, List<Club> clubs) {
        if (isAdmin(actor)) return;
        if (!isPresident(actor) || clubs.stream().noneMatch(c -> c.getId() == clubId && c.getPresidentId() == actor.getId()))
            deny("Only an administrator or the selected club's president can create this event.");
    }

    public void requireSubmit(User actor, Event event, List<Club> clubs) {
        requireCreateEvent(actor, event.getClubId(), clubs);
    }

    public void requireApproval(User actor) {
        if (!isAdmin(actor)) deny("Only administrators can approve or reject events.");
    }

    public void requireRegistration(User actor, int targetUserId) {
        if (!isAdmin(actor) && actor.getId() != targetUserId)
            deny("You can register only your own account.");
    }

    public boolean isAdmin(User user) { return user != null && "ADMIN".equalsIgnoreCase(user.getRole()); }
    public boolean isPresident(User user) { return user != null && "PRESIDENT".equalsIgnoreCase(user.getRole()); }
    private void deny(String message) { throw new BusinessException(message); }
}
