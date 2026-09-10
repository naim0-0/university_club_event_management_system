package com.eventmgmt;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Test;
import com.eventmgmt.models.Club;
import com.eventmgmt.models.User;
import com.eventmgmt.security.AuthorizationService;
import com.eventmgmt.security.PasswordHasher;
import com.eventmgmt.service.BusinessException;

public class SecurityTest {
    @Test
    public void passwordHashUsesSaltAndVerifies() {
        String first = PasswordHasher.hash("secret");
        String second = PasswordHasher.hash("secret");
        assertTrue(PasswordHasher.matches("secret", first));
        assertFalse(PasswordHasher.matches("wrong", first));
        assertFalse(first.equals(second));
    }

    @Test
    public void presidentCanCreateOnlyForOwnClub() {
        User president = new User(2, "President", "p@example.com", "PRESIDENT");
        AuthorizationService access = new AuthorizationService();
        access.requireCreateEvent(president, 1, List.of(new Club(1, "Own Club", 2)));
        assertThrows(BusinessException.class, () -> access.requireCreateEvent(
                president, 3, List.of(new Club(3, "Other Club", 9))));
    }

    @Test
    public void studentCannotApproveOrRegisterSomeoneElse() {
        User student = new User(3, "Student", "s@example.com", "STUDENT");
        AuthorizationService access = new AuthorizationService();
        assertThrows(BusinessException.class, () -> access.requireApproval(student));
        assertThrows(BusinessException.class, () -> access.requireRegistration(student, 7));
        access.requireRegistration(student, 3);
    }
}
